package Controllers;

import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class MainMenuController implements Initializable{


    @FXML private CheckBox showAnimationCheckBox;
    @FXML private Label xmlLoadingLabel;
    @FXML private CheckBox showEquityCheckBox;

    @FXML private ComboBox styleComboBox;
    @FXML private ComboBox animationComboBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Label fileNameLabel;


    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;
    private Stage primaryStage;
    private GameManager gameManager;
    private BusinessLogic businessLogic;

    private boolean showEquity;
    private boolean showAnimation;

    public MainMenuController()
    {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        showEquity=false;
        showAnimation=false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        showEquityCheckBox.setSelected(false);
        showEquityCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                showEquity=new_val;
            }
        });

        showAnimationCheckBox.setSelected(false);
        showAnimationCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                showAnimation=new_val;
            }
        });

        fileNameLabel.setText("Not Loaded");
        startButton.setDisable(true);
        fileNameLabel.textProperty().bind(selectedFileProperty);
        startButton.disableProperty().bind(isFileSelected.not());



        List<String> list = new ArrayList<String>();
        ObservableList<String> observableList = FXCollections.observableList(list);
        observableList.addAll("Style1","Style2","Basic");
        styleComboBox.setItems(observableList);
    }

    @FXML
    public void openFileButtonAction() {

        selectedFileProperty.set("");
        isFileSelected.set(false);
        businessLogic.clearOnlyGameTable();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        String shortName=selectedFile.getName();

        try {

            ReadGameDescriptorFile readGameDescriptorFile=new ReadGameDescriptorFile();
            readGameDescriptorFile.setFilePath(absolutePath);

            if (validateFile(readGameDescriptorFile))
            {
                //file is valid
                selectedFileProperty.set(shortName);
                isFileSelected.set(true);

                gameManager.setGameDescriptor(readGameDescriptorFile.getGameDescriptor());
                gameManager.setTable();
                businessLogic.updateUI();
            }

            else
            {
                //file is not valid
            }
            //ReadGameDescriptorFile readGameDescriptorFile=new ReadGameDescriptorFile();
            //readGameDescriptorFile.setFilePath(absolutePath);
            //xmlLoadingLabel.textProperty().bind(readGameDescriptorFile.messageProperty());
            //readGameDescriptorFile.valueProperty().addListener((observable, oldValue, newValue) -> setSettings(readGameDescriptorFile,newValue));
            //new Thread(readGameDescriptorFile).start();



        } catch (Exception e) {

        }

    }

    private boolean validateFile(ReadGameDescriptorFile readFile) {

        SimpleBooleanProperty res= new SimpleBooleanProperty(true);

        Label label = new Label("Validate XML game file");
        ProgressBar progressBar = new ProgressBar(0);
        ProgressIndicator progressIndicator = new ProgressIndicator(0);

        Button startButton = new Button("Start");
        Button cancelButton = new Button("OK");
        cancelButton.setVisible(false);

            final Label statusLabel = new Label();
            statusLabel.setMinWidth(250);
            statusLabel.setTextFill(Color.BLUE);

            // Start Button.
            startButton.setOnAction(event -> {
                startButton.setDisable(true);
                progressBar.setProgress(0);
                progressIndicator.setProgress(0);

                // Unbind progress property
                progressBar.progressProperty().unbind();

                // Bind progress property
                progressBar.progressProperty().bind(readFile.progressProperty());

                // Hủy bỏ kết nối thuộc tính progress
                progressIndicator.progressProperty().unbind();

                // Bind progress property.
                progressIndicator.progressProperty().bind(readFile.progressProperty());

                // Unbind text property for Label.
                statusLabel.textProperty().unbind();

                // Bind the text property of Label
                // with message property of Task
                statusLabel.textProperty().bind(readFile.messageProperty());

                readFile.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                        t -> {
                            cancelButton.setVisible(true);
                        });

                // Start the Task.
                new Thread(readFile).start();

            });

            Stage popupwindow=new Stage();

            // Cancel
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    res.set(readFile.isValidFile());
                    popupwindow.close();

                }
            });

            FlowPane root = new FlowPane();
            root.setPadding(new Insets(10));
            root.setHgap(10);

            root.getChildren().addAll(label, progressBar, progressIndicator, //
                    statusLabel, startButton, cancelButton);

            Scene scene = new Scene(root, 500, 120, Color.WHITE);
        popupwindow.setTitle("File validation");
        popupwindow.setScene(scene);
        popupwindow.showAndWait();
        return res.get();
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

            loadXmlButton.setDisable(true);
            showEquityCheckBox.setDisable(true);
            isFileSelected.set(false);
            businessLogic.startGame();
        }


    public void enableLoadXMLButton() {
        loadXmlButton.setDisable(false);
    }

    public void enableStartGameButton() {
        startButton.setVisible(true);
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


    public boolean getEquity(){ return showEquity;}

    public boolean getAnimation(){ return showAnimation;}
}
