#!/usr/bin/env ruby
require 'digest'
require 'yaml'

def calc_checksum(method, deploy, files)
  if deploy['file_glob']
    files.each do |filename|
      Dir.glob(filename).each do |file|
        puts "#{method.file(file)} #{file}"
      end
    end
  else
    files.each do |filename|
      puts "#{method.file(filename)} #{filename}"
    end
  end
end

Dir.chdir File.expand_path("../", File.dirname(__FILE__)) do
  config = YAML.load_file('.travis.yml')
  deploy = config['deploy']
  files = Array(deploy['file'])
  [:MD5, :SHA1, :SHA256].each do |method|
    puts "=== START #{method} CHECKSUMS ==="
    calc_checksum(Digest.const_get(method), deploy, files)
    puts "=== END #{method} CHECKSUMS ==="
    puts
  end
end
