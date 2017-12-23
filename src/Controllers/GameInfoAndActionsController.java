package Controllers;

import Engine.GameManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.temporal.TemporalAmount;
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

    private GameManager gameManager;
    private SimpleIntegerProperty big;
    private SimpleIntegerProperty small;
    private SimpleIntegerProperty buy;
    private SimpleIntegerProperty hands;


    public GameInfoAndActionsController()
    {
        big = new SimpleIntegerProperty(0);
        small = new SimpleIntegerProperty(0);
        buy= new SimpleIntegerProperty(0);
        hands= new SimpleIntegerProperty(0);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCardsButton.setDisable(true);
        runNextHandButton.setDisable(true);
        replayButton.setDisable(true);
        prevButton.setDisable(true);
        nextButton.setDisable(true);
    betAmountLabel.setVisible(false);
        raiseAmountLabel.setVisible(false);
        okBetButton.setVisible(false);
        okRaiseButton.setVisible(false);

        bigLabel.textProperty().bind(big.asString());
        smallLabel.textProperty().bind(small.asString());
        handsLabel.textProperty().bind(hands.asString());
        buysLabel.textProperty().bind(buy.asString());
    }

    public void updateGameSettings()
    {
        big.set(gameManager.getBig());
        small.set(gameManager.getSmall());
        hands.set(gameManager.getHandsCount());
        buy.set(gameManager.getBuy());
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
