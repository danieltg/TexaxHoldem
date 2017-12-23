package Controllers;

import Engine.GameManager;
import Engine.Players.PokerPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
@FXML private  GameTableController gameTableController;
@FXML private  GameInfoAndActionsController gameInfoAndActionsController;
@FXML private MainMenuController mainMenuController;
@FXML private PlayersTableController playersTableController;


    private final GameManager gameManager = new GameManager();
    private Stage primaryStage;
    private BusinessLogic businessLogic;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainMenuController.setGameManager(gameManager);
        gameInfoAndActionsController.setGameManager(gameManager);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainMenuController.setPrimaryStage(this.primaryStage);
    }


    public void setBusinessLogic(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
        mainMenuController.setBusinessLogic(this.businessLogic);
    }

    public void setPlayerTable()
    {
        ObservableList<PokerPlayer> pokerPlayers = FXCollections.observableArrayList();
        for(PokerPlayer p: gameManager.getPlayers())
            pokerPlayers.add(p);

        playersTableController.setPlayers(pokerPlayers);
    }

    public void setGameSettings() {
        gameInfoAndActionsController.updateGameSettings();

    }
}
