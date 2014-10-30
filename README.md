JUPAR
=====

Java desktop application updates made easy!

Contributors
------------

1. [Periklis Ntanasis](http://masterex.github.io/archive/2011/12/25/jupar.html) (Author)
2. [Osama Khalid](http://codinghazard.com)

Usage
-----

For pushing an update, you require three files:

1. **latest.xml** (This file contains information about the latest version, release and changelog)
2. **files.xml** (This file contains a list of files to be downloaded)
3. **update.xml** (This file specifies where to move [or perform other actions to] the files that are downloaded)

*You can host your updates at a public location, e.g. Amazon S3 or your own server*

```java
Release current = new Release();
current.setpkgver("1.0");
current.setPkgrel("1");
ReleaseXMLParser parser = new ReleaseXMLParser();
Updater updater = new Updater();
Downloader downloader = new Downloader();

String updateRepositoryUrl = "http://yourbucket.s3.amazonaws.com/updates/";
String temporaryDirectoryForUpdates = "tmp";

Release update = parser.parse(updateRepositoryUrl + "latest.xml", Modes.URL);
if (update.compareTo(current) > 0) {

    // A new update is available, download that update
    downloader.download(updateRepositoryUrl + "files.xml", temporaryDirectoryForUpdates, Modes.URL);
    
    // Move the files downloaded to the appropriate paths
    updater.update("update.xml", temporaryDirectoryForUpdates, Modes.FILE);    
    
    /**
     * Delete tmp directory
     */
    File tmp = new File(temporaryDirectoryForUpdates);
    if (tmp.exists()) {
      for (File file : tmp.listFiles()) {
        file.delete();
      }
      tmp.delete();
    }
}
```

Structure
---------

### latest.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<information>
    <pubDate>Tue, 14 Oct 2014 17:45:13 +0500</pubDate>
    <pkgver>1.0</pkgver>
    <pkgrel>1</pkgrel>
    <severity>normal</severity>
    <extra>
        <message><![CDATA[Your changelog message]]></message>
    </extra>
</information>
```

### files.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<download>
    <file>
    	<filename>http://yourbucket.s3.amazonaws.com/updates/update.xml</filename>
    	<destination>update.xml</destination>
    </file>
    <file>
    	<filename>http://yourbucket.s3.amazonaws.com/updates/files/some-file.jar</filename>
    	<destination>lib/some-file.jar</destination>
    	<hash>33e1791669c88edf805d4091da60abbe891a5b67</hash>
    </file>
</download>
```

The **hash** field contains the SHA-1 hash of the file, it is used to check if the file at **destination** has a different hash, and the file is only downloaded if it is different from the original. It saves a lot of bandwidth.

### update.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<update>
    <instruction>
        <action>MOVE</action>
        <file>some-file.jar</file>
        <destination>lib/some-file.jar</destination>
    </instruction>
</update>
```

The action types are MOVE, EXECUTE and DELETE.
