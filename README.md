# DMARC Reader - JavaFX
Presents an XML DMARC report in a nicer way.

The class [`DmarcXmlDocumentHandler.java`](src/main/java/ph/kana/dmarcreader/xml/DmarcXmlDocumentHandler.java) handles most of the parsing logic. Uses [SAX](https://docs.oracle.com/javase/tutorial/jaxp/sax/index.html) to parse XML files.
If something breaks in the XML parsing, it's probably there.

Building are usually done using `jlink`.
```shell
$ ./gradlew jlink
```

JDK Version/Build: `17.0.1-tem`
Gradle (wrapper): `7.3.1`
Other libraries: Visit [`build.gradle`](build.gradle) file.
