package Controllers;

import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class MainMenuController implements Initializable{

    @FXML private Label xmlLoadingLabel;
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



        List<String> list = new ArrayList<String>();
        ObservableList<String> observableList = FXCollections.observableList(list);
        observableList.addAll("Style1","Style2","Basic");
        //noinspection unchecked
        styleComboBox.setItems(observableList);
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
            ReadGameDescriptorFile readGameDescriptorFile=new ReadGameDescriptorFile();
            readGameDescriptorFile.setFilePath(absolutePath);
            xmlLoadingLabel.textProperty().bind(readGameDescriptorFile.messageProperty());
            readGameDescriptorFile.valueProperty().addListener((observable, oldValue, newValue) -> setSettings(readGameDescriptorFile,newValue));
            new Thread(readGameDescriptorFile).start();



        } catch (Exception e) {

        }

    }

public void setSettings(ReadGameDescriptorFile readGameDescriptorFile,Boolean result)
{
    if(result) {
        gameManager.setGameDescriptor(readGameDescriptorFile.getGameDescriptor());
        gameManager.setTable();
        businessLogic.updateUI();
    }

}

    public  void setPrimaryStage (Stage s) { primaryStage=s; }

    public void setGameManager(GameManager g) { gameManager=g; }

    public void setBusinessLogic(BusinessLogic b){businessLogic=b;}



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


    public void enableLoadXMLButton() {
        loadXmlButton.setDisable(false);
    }

    public void enableStartGameButton() {
        startButton.setVisible(true);
        //startButton.setDisable(false);
    }

    public void styleChanged(ActionEvent actionEvent) {
        if(styleComboBox.getSelectionModel().getSelectedItem().toString()=="Style1")
        {
            businessLogic.changeToStyle1();
        }
        else if(styleComboBox.getSelectionModel().getSelectedItem().toString()=="Style2")
        {
            businessLogic.changeToStyle2();
        }
        else
            businessLogic.basicStyle();
    }
}
