package Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable{

    @FXML private ComboBox styleComboBox;
    @FXML private ComboBox animationComboBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Label fileNameLabel;
    @FXML private Button stopButton;

    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;
    private Stage primaryStage;

    public MainMenuController()
    {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileNameLabel.setText("Not Loaded");
        startButton.setDisable(true);
        stopButton.setDisable(true);
        fileNameLabel.textProperty().bind(selectedFileProperty);
        startButton.disableProperty().bind(isFileSelected.not());
    }

    @FXML
    public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getName();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

    }


}
