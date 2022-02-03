package ph.kana.dmarcreader.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.probeContentType;

public class FileService {

    public InputStream openFile(File file) {
        var path = file.toPath();
        try {
            var mimeType = probeContentType(path);
            var inputStream = newInputStream(path);

            return switch (mimeType) {
                case "application/xml", "text/xml" -> inputStream;
                case "application/zip" -> decompressZip(inputStream);
                case "application/gzip" -> new GZIPInputStream(inputStream);
                default -> throw new DmarcIoException("Invalid MIME type! mimeType=" + mimeType);
            };
        } catch (IOException e) {
            throw new DmarcIoException(String.format("Failed to open file! file=%s", file.getAbsolutePath()), e);
        }
    }

    private static InputStream decompressZip(InputStream inputStream) throws IOException {
        var zipInputStream = new ZipInputStream(inputStream);
        zipInputStream.getNextEntry();
        return zipInputStream;
    }
}
