#!/usr/bin/env ruby
require 'colorize'
require 'active_support/core_ext/object/blank'
require 'active_support/core_ext/string'

class Lang < Struct.new(:filename, :table)
end

def parse_lang_file(contents, filename)
  result = {}
  contents.each_line.each_with_index do |line, i|
    str = line.chomp
    if str =~ /\A#/
      # line is a comment
    elsif str =~ /\A([\w\.\-_\|]+)\s*=(.*)/
      result[$1] = $2
    elsif str.blank?
      # line is empty
    else
      warn "WARN: Odd line in \"#{filename}:#{i + 1}\" #{str}"
    end
  end
  result
end

def load_lang_file(filename)
  parse_lang_file File.read(filename), File.expand_path(filename)
end

# This utility script will cross-check all lang files against the en_US one to
# check for missing entries
base_lang_name = 'en_US'
Dir.chdir File.expand_path('../', __dir__) do
  root_path = Dir.getwd
  Dir.chdir 'src/resources/assets' do
    modules = ARGV.presence || Dir.glob("./*").select { |d| File.directory?(d) }.map { |d| File.basename(d) }

    modules.each do |m|
      Dir.glob("#{m}/lang/#{base_lang_name}.lang") do |en_filename|
        module_name = File.basename File.dirname File.dirname en_filename
        Dir.chdir File.dirname(en_filename) do
          langs = {}
          Dir.glob("*.lang") do |filename|
            langs[File.basename(filename, '.lang')] = Lang.new(File.expand_path(filename).gsub(/\A#{root_path}\//, ''), load_lang_file(filename))
          end
          base_lang = langs.fetch(base_lang_name)

          # ensure that anything in the base lang is in the other langs
          langs.each_pair do |lang_name, lang|
            puts "#{module_name} #{lang_name} (#{lang.filename})"
            everything_ok = true
            base_lang.table.each_pair do |name, _|
              if lang.table.key?(name)
                if lang.table[name].blank?
                  STDERR.puts "\tERROR: Mapping for #{name} is empty!".light_red
                  everything_ok = false
                end
              else
                STDERR.puts "\tERROR: No mapping for #{name}".light_red
                everything_ok = false
              end
            end

            STDOUT.puts "\tOK".light_green if everything_ok
          end
        end
      end
    end
  end
end
