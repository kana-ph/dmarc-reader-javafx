package ph.kana.dmarcreader.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import ph.kana.dmarcreader.io.DmarcIoException;
import ph.kana.dmarcreader.io.FileService;
import ph.kana.dmarcreader.model.DmarcFeedback;
import ph.kana.dmarcreader.model.DmarcRecord;
import ph.kana.dmarcreader.xml.DmarcXmlException;
import ph.kana.dmarcreader.xml.DmarcXmlParser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    private static final String NO_FILE_ALERT = """
        Drag DMARC report file here to read report!
        Alternatively, Click here to select a DMARC report file!
        (Accepts .gz / .zip / .xml formats)""";
    private static final File INITIAL_OPEN_DIRECTORY = new File(System.getProperty("user.home"));
    private static final List<FileChooser.ExtensionFilter> EXTENSION_FILTERS = List.of(
        new FileChooser.ExtensionFilter("All supported files (*.xml, *.zip, *.gz)", "*.xml", "*.zip", "*.gz"),
        new FileChooser.ExtensionFilter("XML Text Files", "*.xml"),
        new FileChooser.ExtensionFilter("ZIP Archives", "*.zip"),
        new FileChooser.ExtensionFilter("GZ Files", "*.gz")
    );

    private DmarcFeedback dmarcFeedback = null;
    private final DmarcXmlParser dmarcXmlParser = new DmarcXmlParser();
    private final FileService fileService = new FileService();

    @FXML
    private Pane fileDropPane;

    @FXML
    private Label openFileLabel;

    @FXML
    private TextField reporterTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField extraContactTextField;

    @FXML
    private TextField reportIdTextField;

    @FXML
    private TextField dateFromTextField;

    @FXML
    private TextField dateToTextField;

    @FXML
    private TextField domainTextField;

    @FXML
    private TextField adkimTextField;

    @FXML
    private TextField aspfTextField;

    @FXML
    private TextField pTextField;

    @FXML
    private TextField spTextField;

    @FXML
    private TextField pctTextField;

    @FXML
    private TableView<DmarcTableRecord> recordsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateFileDropMessage(NO_FILE_ALERT, "alert-default");
        initializeTableColumns();
        recordsTable.requestFocus();
    }

    @FXML
    public void openFile() {
        var fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(INITIAL_OPEN_DIRECTORY);
        fileChooser.getExtensionFilters()
            .addAll(EXTENSION_FILTERS);
        fileChooser.setSelectedExtensionFilter(EXTENSION_FILTERS.get(0));

        var selectedFile = fileChooser.showOpenDialog(openFileLabel.getScene().getWindow());
        if (selectedFile != null) {
            openFile(selectedFile);
        }
    }

    @FXML
    public void dragOver(DragEvent event) {
        if (event.getGestureSource() != fileDropPane && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    public void dragDropped(DragEvent event) {
        var dragboard = event.getDragboard();
        var completed = false;
        if (dragboard.hasFiles()) {
            var draggedFile = dragboard.getFiles()
                .get(0);
            openFile(draggedFile);
            completed = true;
        }
        event.setDropCompleted(completed);
        event.consume();
    }

    private void initializeTableColumns() {
        var columns = recordsTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("sourceIp"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("count"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("disposition"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("spf"));
        columns.get(4).setCellValueFactory(new PropertyValueFactory<>("dkim"));
        columns.get(5).setCellValueFactory(new PropertyValueFactory<>("headerFrom"));
        columns.get(6).setCellValueFactory(new PropertyValueFactory<>("domainSpfResult"));
        columns.get(7).setCellValueFactory(new PropertyValueFactory<>("domainDkimResult"));
    }

    private void loadDmarcToUi() {
        var metadata = dmarcFeedback.getReportMetadata();
        reporterTextField.setText(metadata.getOrgName());
        emailTextField.setText(metadata.getEmail());
        extraContactTextField.setText(metadata.getExtraContact());
        reportIdTextField.setText(metadata.getReportId());
        dateFromTextField.setText(metadata.getDateRange().getBegin().toString());
        dateToTextField.setText(metadata.getDateRange().getEnd().toString());

        var policy = dmarcFeedback.getPolicyPublished();
        domainTextField.setText(policy.getDomain());
        adkimTextField.setText(policy.getDkimAlignment());
        aspfTextField.setText(policy.getSpfAlignment());
        pTextField.setText(policy.getDomainPolicy());
        spTextField.setText(policy.getSubdomainPolicy());
        pctTextField.setText(policy.getPercentage() + "%");

        var dmarcTableRecords = convertToTableRecords(dmarcFeedback.getRecords());
        recordsTable.setItems(dmarcTableRecords);
    }

    private ObservableList<DmarcTableRecord> convertToTableRecords(List<DmarcRecord> records) {
        return records.stream()
            .map(DmarcTableRecord::new)
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private void updateFileDropMessage(String message, String alertCssClass) {
        openFileLabel.setText(message);
        var styleClasses = fileDropPane.getStyleClass();
        styleClasses.removeIf(className -> className.startsWith("alert"));
        styleClasses.addAll("alert", alertCssClass);
    }

    private void openFile(File file) {
        try {
            var inputStream = fileService.openFile(file);
            this.dmarcFeedback = dmarcXmlParser.parse(inputStream);
            var message = "Viewing DMARC Report File:\n" + file.getAbsolutePath();
            updateFileDropMessage(message, "alert-success");

            loadDmarcToUi();
        } catch (DmarcIoException | DmarcXmlException e) {
            var message = String.format("ERROR opening file %s\nReason: %s\nCheck if the file is valid and try again.", file.getAbsolutePath(), e.getMessage());
            updateFileDropMessage(message, "alert-danger");
        }
    }
}
