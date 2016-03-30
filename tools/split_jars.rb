#!/usr/bin/env ruby
require 'json'
require 'fileutils'
require 'tmpdir'
require 'optparse'

@verbose = false

argv = OptionParser.new do |opts|
  opts.on '-v', '--verbose', 'Be noisy about stuff' do |v|
    @verbose = v
  end
end.parse(ARGV)

def verbose?
  @verbose
end

def verbose(*args)
  puts(*args) if verbose?
end

def sh(cmd)
  verbose cmd
  system(cmd) || fail("Command Failed")
end

mcmod_info = JSON.parse File.read("src/resources/mcmod.info")

# First we'll need to extract each pack's mcmod info from the given list
pack_mcmod = mcmod_info.each_with_object({}) do |mcmod, result|
  packname = mcmod.fetch('modid').downcase
  packname = if packname == 'growthcraft'
    'core'
  else
    packname.gsub('growthcraft|', '')
  end
  result[packname] = mcmod
end

# now we create our target packages directory
FileUtils.mkdir_p 'build/packages'

# going through each of the existing lib jars and try to create a package set
# for it
Dir.glob File.expand_path("../build/libs/growthcraft-*.jar", File.dirname(__FILE__)) do |f|
  # blacklist these jars
  next if f =~ /-(api|javadoc|sources)/

  # to avoid all kinds of craziness, just create a tmpdir and extract
  # the jar's contents there
  Dir.mktmpdir 'grctmp_' do |dir|
    # for some reason "jar x" froze on my system
    verbose "\tExtracting #{f} to #{dir}/content"
    dirname = "#{dir}/content"
    FileUtils.mkdir_p dirname
    cmd_str = " \"#{f}\" -d\"#{dirname}\""
    cmd = "unzip -o"
    if verbose?
      sh(cmd + cmd_str)
    else
      sh(cmd + " -q " + cmd_str) || fail("Extraction failed")
    end

    # strip the dirname and extname off the parent jar, this will be
    # used for the packages basename
    base = File.basename(f, File.extname(f))

    # generate each package jar by using the contents from the extracted
    # parent jar and resaved mcmod.info specific to the package
    pack_mcmod.each_key do |packname|
      mcmod = pack_mcmod.fetch(packname)
      #pack_version = mcmod.fetch('version')
      filename = [base, packname].compact.join('-') + '.jar'

      verbose "\tBuilding package #{filename}"

      verbose "\tWriting mcmod.info for #{filename}"
      # spit out the specific mcmod.info for this pack, and make it look
      # PRETTY.
      File.write(File.join(dir, "mcmod.info"), JSON.pretty_generate([mcmod]))

      verbose "\tCreating jar #{filename}"
      vflag = (verbose? && 'v') || ''
      # go ahead, SUE ME for using system, I don't feel like fighting with
      # ruby zip and `jar` does the job best.
      content = [
        %(-C "#{dir}" mcmod.info),
        %(-C "#{dir}/content" growthcraft/#{packname}),
        %(-C "#{dir}/content" grc_#{packname}_logo.png),
        %(-C "#{dir}/content" assets/grc#{packname})
      ]
      # core requires the api as well -.-;
      content << %(-C "#{dir}/content" growthcraft/api) if packname == 'core'
      cmd = %(jar #{vflag}cf build/packages/#{filename} ) + content.join(" ")
      sh cmd
    end
  end
end
