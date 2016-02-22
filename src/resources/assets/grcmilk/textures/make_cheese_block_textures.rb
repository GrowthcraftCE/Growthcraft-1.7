require 'minil'
require 'fileutils'

Dir.glob("models/cheese/*.png").each do |png|
  image = Minil::Image.load_file(png)
  dirname = png.gsub(/\Amodels/, 'blocks').gsub(/\.png\z/, '')
  FileUtils::Verbose.mkdir_p dirname unless Dir.exist?(dirname)
  begin
    subimg = image.subimage(8, 26, 8, 8)
    img = Minil::Image.create(16, 16)
    img.blit(subimg, 0, 0, 0, 0, subimg.width, subimg.height)
    img.blit(subimg, subimg.width, 0, 0, 0, subimg.width, subimg.height)
    img.blit(subimg, subimg.width, subimg.height, 0, 0, subimg.width, subimg.height)
    img.blit(subimg, 0, subimg.height, 0, 0, subimg.width, subimg.height)
    img.save_file File.join(dirname, 'top.png')
  end
  begin
    subimg = image.subimage(0, 34, 16, 8)
    img = Minil::Image.create(16, 16)
    img.blit(subimg, 0, 0, 0, 0, subimg.width, subimg.height)
    img.blit(subimg, 0, subimg.height, 0, 0, subimg.width, subimg.height)
    img.save_file File.join(dirname, 'side.png')
    img.save_file File.join(dirname, 'bottom.png')
  end
end
