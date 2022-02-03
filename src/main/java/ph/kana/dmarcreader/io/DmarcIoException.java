package ph.kana.dmarcreader.io;

public class DmarcIoException extends RuntimeException {

    public DmarcIoException(String message) {
        super(message);
    }

    public DmarcIoException(String message, Throwable cause) {
        super(message, cause);
    }
}
