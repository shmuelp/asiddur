# Introduction #

This pages describes how to build from sources.  The current directions assume Linux, although the steps should be similar (identical?) on other platforms.  They also assume development on the trunk.

# Details #

  1. Check out the project:
> > `svn checkout http://asiddur.googlecode.com/svn/trunk/ asiddur`
  1. Download and install [NetBeans](http://www.netbeans.info/downloads/index.php) (5.0 is currently used, 5.5 works as well).  Download the bundle with an included JDK if you don't already have one installed.
  1. Download and install the [NetBeans Mobility Pack](http://www.netbeans.info/downloads/index.php?rs=11&p=4).
  1. Open NetBeans, and choose File | Open Project...
    * The project directory is the `asiddur` subdirectory of the directory to which you downloaded the source in step #1 above.
  1. Also open the project based in the `common/ASiddurCommon`
    * To work on the desktop program that converts an XML-based tefilla into a binary image, also open and build the `tools/text-converter/TefillaConverter` project.
  1. Build each project.  Build `ASiddurCommon` first.  You may need to replace any text in the project files that contains `/home/shmuelp/Projects/` with the directory you are working  in.  I don't know how to abstract that out of the project files, but I haven't tried all that hard.
    * The resulting JAR and JAD files are in the `dist/` subdirectory.