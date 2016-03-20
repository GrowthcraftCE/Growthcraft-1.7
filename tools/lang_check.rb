#!/usr/bin/env ruby
require_relative 'lib/language_file'

def load_lang_file(filename)
  LangCheck::LanguageFile.load_file(filename)
end

# This utility script will cross-check all lang files against the en_US one to
# check for missing entries
base_lang_name = 'en_US'
Dir.chdir File.expand_path('../', __dir__) do
  root_path = Dir.getwd
  Dir.chdir 'src/resources/assets' do
    modules = ARGV.presence || Dir.glob("./*").select { |d| File.directory?(d) }.map { |d| File.basename(d) }

    languages = Dir.glob("**/*.lang").map { |fn| File.basename(fn, ".lang") }.uniq.sort

    modules.each do |m|
      Dir.glob("#{m}/lang/#{base_lang_name}.lang") do |en_filename|
        module_name = File.basename File.dirname File.dirname en_filename
        Dir.chdir File.dirname(en_filename) do
          langs = {}
          languages.each do |lang_basename|
            lang_filename = "#{lang_basename}.lang"
            langs[lang_basename] = if File.exist?(lang_filename)
              load_lang_file(lang_filename)
            else
              nil
            end
          end
          base_lang = langs[base_lang_name]

          if base_lang
            # ensure that anything in the base lang is in the other langs
            langs.each_pair do |lang_name, lang|
              if lang
                puts "#{module_name} #{lang_name} (#{lang.filename})"
                everything_ok = true
                base_lang.table.each_pair do |name, _|
                  if lang.table.key?(name)
                    if lang.table[name].blank?
                      STDERR.puts "\tERROR: Mapping for `#{name}` is empty!".light_red
                      everything_ok = false
                    end
                  else
                    STDERR.puts "\tERROR: No mapping for `#{name}`".light_red
                    everything_ok = false
                  end
                end
                extra_keys = lang.table.keys - base_lang.table.keys
                if extra_keys.empty?
                  STDOUT.puts "\tOK".light_green if everything_ok
                else
                  extra_keys.each do |key|
                    STDERR
                  end
                end
              else
                STDERR.puts "\tWARN: Translation `#{lang_name}` is missing for module `#{module_name}`"
              end
            end
          else
            STDERR.puts "\tCRITICAL-ERROR: `#{module_name}` has no `#{base_lang_name}` translation!".light_red
          end
        end
      end
    end
  end
end
