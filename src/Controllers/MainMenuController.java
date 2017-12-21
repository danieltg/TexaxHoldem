package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable{
@FXML private ComboBox styleComboBox;
@FXML private ComboBox animationComboBox;
@FXML private Button loadXmlButton;
@FXML private Button startButton;
@FXML private Label fileNameLabel;
@FXML private Button stopButton;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
            fileNameLabel.setText("Not Loaded");
            startButton.setDisable(true);
            stopButton.setDisable(true);
    }
}
