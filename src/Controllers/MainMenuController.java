package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable{
@FXML ComboBox styleComboBox;
@FXML ComboBox animationComboBox;
@FXML Button loadXmlButton;
@FXML Button startButton;
@FXML Label fileNameLabel;
@FXML Button stopButton;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
            fileNameLabel.setText("Not Loaded");
    }
}
