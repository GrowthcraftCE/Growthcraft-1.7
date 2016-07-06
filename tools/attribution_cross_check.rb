#!/usr/bin/env ruby
require 'json'

root = File.expand_path('../', File.dirname(__FILE__))

Dir.chdir root do
  attributions = JSON.load(File.read('ATTRIBUTION.json'))
  files = attributions['files']
  Dir.glob("src/**/*.png") do |filename|
    unless files.key?(filename)
      puts "MISSING-ATTRIBUTION #{filename}"
    end
  end

  files.each do |filename, _|
    unless File.exist?(filename)
      puts "MISSING-FILE #{filename}"
    end
  end
end
