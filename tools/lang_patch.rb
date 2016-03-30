#!/usr/bin/env ruby
require_relative 'lib/language_file'

def print_module_list(modules)
  puts "Available Modules:"
  modules.each do |mod|
    puts "\t#{mod.light_green}"
  end
end

def print_languages_list(languages)
  puts "Available Languages:"
  languages.each do |lang|
    puts "\t#{lang.light_green}"
  end
end

def load_language_files(languages)
  languages.each_with_object({}) do |lang, language_files|
    lang_filename = File.join('lang', lang + '.lang')
    language_files[lang] = LangCheck::LanguageFile.load_file(lang_filename)
  end
end

def patch_languages_for_mod(options)
  languages = options.fetch(:languages)
  base_lang_name = options.fetch(:base_lang_name)
  mod = options.fetch(:module)

  Dir.chdir mod do
    puts "CD #{Dir.getwd}"
    language_files = load_language_files(languages)
    base_lang = language_files.fetch(base_lang_name)
    if base_lang.has_content?
      languages.each do |lang_name|
        next if lang_name == base_lang_name
        lang = language_files.fetch(lang_name)
        contents = base_lang.table.cross_generate(lang && lang.table)
        puts "Writing #{lang.filename}".light_blue
        File.write(lang.filename, contents)
      end
    else
      puts "\tERROR: `#{mod}` cannot patch languages, there is no base_lang `#{base_lang_name}`"
    end
  end
end

def patch_module_langs(options)
  languages = options.fetch(:languages)
  modules = options.fetch(:modules)
  base_lang_name = options.fetch(:base_lang_name)

  modules.each do |mod|
    lang_dir = File.join(mod, 'lang')
    if Dir.exist?(lang_dir)
      patch_languages_for_mod(module: mod, languages: languages, base_lang_name: base_lang_name)
    else
      puts "\tWARN: `#{mod}` has no lang directory"
    end
  end
end

# This utility script will cross-check all lang files against the en_US one to
# check for missing entries
base_lang_name = 'en_US'

Dir.chdir File.expand_path('../src/resources/assets', File.dirname(__FILE__)) do
  puts "CD #{Dir.getwd}"
  modules = Dir.glob("*").select { |d| File.directory?(d) }.map { |d| File.basename(d) }.sort
  languages = Dir.glob("**/*.lang").map { |fn| File.basename(fn, ".lang").strip }.uniq.sort

  print_module_list(modules)
  print_languages_list(languages)
  puts "Base Language is: #{base_lang_name.light_green}"
  patch_module_langs(modules: modules, languages: languages, base_lang_name: base_lang_name)
end
