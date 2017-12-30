package Controllers;

import Engine.Exceptions.BlindesException;
import Engine.Exceptions.GameStateException;
import Engine.Exceptions.StructureException;
import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable{

    @FXML private ColorPicker tableColor;
    @FXML private ComboBox styleComboBox;
    @FXML private ComboBox animationComboBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Label fileNameLabel;
    @FXML private Button stopButton;

    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;
    private Stage primaryStage;
    private GameManager gameManager;
    private BusinessLogic businessLogic;

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

        try {
            gameManager.setGameDescriptor(ReadGameDescriptorFile.readFile(selectedFile.getAbsolutePath()));
            gameManager.setTable();
            businessLogic.updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public  void setPrimaryStage (Stage s) { primaryStage=s; }

    public void setGameManager(GameManager g) { gameManager=g; }

    public void setBusinessLogic(BusinessLogic b){businessLogic=b;}

    public void changeColor(ActionEvent actionEvent) {

        businessLogic.changeColor(tableColor.getValue());
    }

    public void startGameButtonAction(ActionEvent actionEvent) {

            stopButton.setDisable(false);
            loadXmlButton.setDisable(true);
            startButton.setVisible(false);
            businessLogic.startGame();
        }


    public void stopClicked(ActionEvent actionEvent) {
        startButton.setVisible(true);
        //TODO:: finish game
    }
}
