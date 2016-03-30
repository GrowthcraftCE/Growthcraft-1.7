#!/usr/bin/env ruby
require 'colorize'
require 'active_support/core_ext/object/blank'
require 'active_support/core_ext/string'

module LangCheck
  class LanguageTable
    class TranslationLine
      attr_accessor :key
      attr_accessor :value
      attr_accessor :raw
      attr_accessor :line

      # @param [Hash<Symbol, Object>] options
      def initialize(options)
        self.key = options.fetch(:key)
        self.value = options.fetch(:value)
        self.raw = options.fetch(:raw)
        self.line = options.fetch(:line)
      end
    end

    attr_accessor :filename
    attr_accessor :contents
    attr_accessor :content_lines

    # @param [Hash<Symbol, Object>] options
    def initialize(options)
      self.filename = options.fetch(:filename)
      self.contents = options.fetch(:contents)
      self.content_lines = []
      @data = {}
      load_lang_file
    end

    def key?(key)
      @data.key?(key)
    end

    def add(options)
      key = options.fetch(:key)
      if key?(key)
        original = @data[key]
        warn %Q(\tWARN: Duplicate translation entry in "#{source_line}": `#{key}`=`#{value}` first defined here `#{line_map[key]}` as `#{original}`).light_yellow
      end
      @data[key] = TranslationLine.new(options)
    end

    def keys
      @data.keys
    end

    def each_pair
      @data.each_pair do |key, value|
        yield key, (value ? value.value : nil)
      end
    end

    def [](key)
      tl = @data[key]
      tl && tl.value
    end

    def load_lang_file
      line_map = {}
      contents.each_line.each_with_index do |line, i|
        str = line.chomp
        source_line = "#{filename}:#{i + 1}"
        tl = TranslationLine.new(key: nil, value: nil, raw: str, line: i)
        line = if str.blank?
          # line is empty
          tl
        elsif str =~ /\A#/
          # the line is a comment
          tl
        elsif str =~ /\A([\w\.\-_\|]+)\s*=(.*)/
          key, value = $1, $2
          add(key: key, value: value, raw: str, line: i)
        else
          warn %Q(\tWARN: Possibly Malformed line in "#{source_line}": `#{str}`).light_yellow
          tl
        end
        content_lines << line
      end
      self
    end

    # @param [LanguageTable] filler_lang
    def cross_generate(filler_lang)
      result = []
      content_lines.each do |line|
        if line.key.present?
          value = (filler_lang && filler_lang[line.key].presence) || line.key
          result << "#{line.key}=#{value}"
        else
          result << line.raw
        end
      end
      result.join("\n")
    end
  end

  def self.load_lang_table(contents, options)
    LanguageTable.new options.merge(contents: contents)
  end

  class LanguageFile
    attr_accessor :filename
    attr_accessor :contents
    attr_accessor :table

    def initialize(options)
      self.filename = options.fetch(:filename)
      self.contents = options.fetch(:contents)
      self.table = options.fetch(:table)
    end

    def has_content?
      contents.present?
    end

    def self.load_file(filename)
      contents = File.exist?(filename) ? File.read(filename) : ''
      new filename: filename, contents: contents, table: LangCheck.load_lang_table(contents, filename: filename)
    end
  end
end
