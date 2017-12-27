package Controllers;

import Engine.GameManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.temporal.TemporalAmount;
import java.util.ResourceBundle;

import static javafx.beans.binding.Bindings.not;

public class GameInfoAndActionsController implements Initializable{

    @FXML private Accordion accordionGame;
    @FXML private TitledPane gameSettings;
    @FXML private TitledPane gameDetails;
    @FXML private GridPane gameSettingsGrid;
    @FXML private Label handsLabel;
    @FXML private Label buysLabel;
    @FXML private Label bigLabel;
    @FXML private Label  smallLabel;
    @FXML private CheckBox isFixedCheckBox;
    @FXML private Label additionsLabel;
    @FXML private Label additionsValue;
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
    private SimpleIntegerProperty additions;
    private SimpleBooleanProperty isFixed;

    private SimpleIntegerProperty handsCount;
    private SimpleIntegerProperty maxPOT;
    private String selection;

    public GameInfoAndActionsController()
    {
        big = new SimpleIntegerProperty(0);
        small = new SimpleIntegerProperty(0);
        buy= new SimpleIntegerProperty(0);
        hands= new SimpleIntegerProperty(0);
        additions= new SimpleIntegerProperty(0);
        isFixed= new SimpleBooleanProperty(true);
        handsCount=new SimpleIntegerProperty(1);
        maxPOT=new SimpleIntegerProperty(0);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        raiseButton.setDisable(true);
        raiseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="R";
            }
        });

        checkButton.setDisable(true);
        checkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="K";
            }
        });

        betButton.setDisable(true);
        betButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="B";
            }
        });

        callButton.setDisable(true);
        callButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="C";
            }
        });

        foldButton.setDisable(true);
        foldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="F";
            }
        });


        gameSettings.setCollapsible(false);
        gameDetails.setCollapsible(false);

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
        totalHandsLabel.textProperty().bind(hands.asString());
        handsCountLabel.textProperty().bind(handsCount.asString());

        buysLabel.textProperty().bind(buy.asString());
        additionsValue.textProperty().bind(additions.asString());
        isFixedCheckBox.selectedProperty().bind(isFixed);

        additionsValue.visibleProperty().bind(not(isFixed));
        additionsLabel.visibleProperty().bind(not(isFixed));

        maxPotLabel.textProperty().bind(maxPOT.asString());
    }

    public void updateGameSettings()
    {
        gameSettings.setCollapsible(true);

        big.set(gameManager.getBig());
        small.set(gameManager.getSmall());
        hands.set(gameManager.getHandsCount());
        buy.set(gameManager.getBuy());
        additions.set(gameManager.getAdditions());
        isFixed.set(gameManager.getIsFixed());

        gameSettings.setExpanded(true);
    }

    public void updateGameDetails()
    {
        gameDetails.setCollapsible(true);

        handsCount.set(gameManager.handNumber);
        maxPOT.set(gameManager.getMaxPot());
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    public String getUserSelection()
    {
        //selection=null;
        raiseButton.setDisable(false);
        checkButton.setDisable(false);
        betButton.setDisable(false);
        callButton.setDisable(false);
        foldButton.setDisable(false);


        return "F";
    }
}
