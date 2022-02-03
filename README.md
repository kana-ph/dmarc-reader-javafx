# DMARC Reader - JavaFX
Presents an XML DMARC report in a nicer way.

Does basic DMARC report presentation: Report metadata, policy published, and the DMARC records.

Supports opening file using a file dialog or dropping a file to the large box at the top of the main window.

Accepts ZIP, GZ, and XML files. ZIP and GZ files are automatically decompressed to raw XML prior to presentation.

The epochs in `<date_range>` object is presented as human-readable date/time in your system timezone.

Does not handle presentation of mail forwarding yet; I don't have a sample file to play around.

The class [`DmarcXmlDocumentHandler.java`](src/main/java/ph/kana/dmarcreader/xml/DmarcXmlDocumentHandler.java) handles most of the parsing logic. Uses [SAX](https://docs.oracle.com/javase/tutorial/jaxp/sax/index.html) to parse XML files.
If something breaks in the XML parsing, it's probably there.

Building are usually done using `jlink`.
```shell
$ ./gradlew jlink
```

- JDK Version/Build: `17.0.1-tem`
- Gradle (wrapper): `7.3.1`
- Other libraries: Visit [`build.gradle`](build.gradle) file.
