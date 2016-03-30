#!/usr/bin/env ruby
BOM = "\u{feff}"

Dir.chdir File.expand_path('../', File.dirname(__FILE__)) do
  Dir.chdir 'src/resources/assets' do
    Dir.glob("**/lang/*.lang") do |filename|
      contents = File.read(filename)
      if contents.include?(BOM)
        puts filename
        File.write(filename, contents.gsub(BOM, ''))
      end
    end
  end
end
