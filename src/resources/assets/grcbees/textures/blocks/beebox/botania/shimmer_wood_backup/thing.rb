#!/usr/bin/env ruby
require 'minil'

['bottom.png', 'side.png', 'side_honey.png', 'top.png'].each do |filename|
  img = Minil::Image.load_file(filename)
  result = Minil::Image.create(img.width, img.height * 18)
  18.times do |y|
    result.blit(img, 0, y * 16, 0, 0, img.width, img.height)
  end
  result.save_file(File.join("../shimmer_wood", filename))
end
