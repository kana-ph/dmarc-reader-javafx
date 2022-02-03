module ph.kana.dmarcreader {
    requires org.kordamp.bootstrapfx.core;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens ph.kana.dmarcreader.ui to javafx.fxml;
    exports ph.kana.dmarcreader;
    exports ph.kana.dmarcreader.ui;
}
