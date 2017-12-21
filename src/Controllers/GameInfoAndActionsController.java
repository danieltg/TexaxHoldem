package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ResourceBundle;

public class GameInfoAndActionsController implements Initializable{

    @FXML private Accordion accordionGame;
    @FXML private TitledPane gameSettings;
    @FXML private GridPane gameSettingsGrid;
    @FXML private Label handsLabel;
    @FXML private Label buysLabel;
    @FXML private Label bigLabel;
    @FXML private Label  smallLabel;
    @FXML private CheckBox isFixedCheckBox;
    @FXML private Label additionsLabel;
    @FXML private Label handsCountLabel;
    @FXML private Label totalHandsLabel;
    @FXML private Label maxPotLabel;
    @FXML private ImageView firstCardImage;
    @FXML private ImageView secondCardImage;
    @FXML private Button showCardsButton;
    @FXML private Button runNextHandButton;
    @FXML private Button  replayButton;
    @FXML private Button  prevButton;
    @FXML private Button nextButton;
    @FXML private Button  raiseButton;
    @FXML private Button  checkButton;
    @FXML private Button  betButton;
    @FXML private Button  callButton;
    @FXML private Button  foldButton;
    @FXML private Button  okRaiseButton;
    @FXML private Button  okBetButton;
    @FXML private TextField raiseAmountLabel;
    @FXML private TextField betAmountLabel;









    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
