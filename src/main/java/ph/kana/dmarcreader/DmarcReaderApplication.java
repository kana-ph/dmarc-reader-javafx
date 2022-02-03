package ph.kana.dmarcreader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class DmarcReaderApplication extends Application {

    private static final double APP_WIDTH = 800;
    private static final double APP_HEIGHT = 610;
    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(DmarcReaderApplication.class.getResource("ui/main-view.fxml"));
        var scene = new Scene(fxmlLoader.load(), APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets()
            .add(BootstrapFX.bootstrapFXStylesheet());

        stage.setTitle("kana.ph - DMARC Reader");
        stage.setScene(scene);
        stage.setMinHeight(APP_HEIGHT);
        stage.setMinWidth(APP_WIDTH);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
