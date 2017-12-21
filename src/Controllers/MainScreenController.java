package Controllers;

import Engine.GameManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private GameInfoAndActionsController gameInfoAndActionsController;
    private GameTableController gameTableController;
    private MainMenuController mainMenuController;
    private PlayersTableController playersTableController;

    private GameManager gameManager;
    private Stage primaryStage;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public GameInfoAndActionsController getGameInfoAndActionsController() {
        return gameInfoAndActionsController;
    }

    public void setGameInfoAndActionsController(GameInfoAndActionsController gameInfoAndActionsController) {
        this.gameInfoAndActionsController = gameInfoAndActionsController;
    }

    public GameTableController getGameTableController() {
        return gameTableController;
    }

    public void setGameTableController(GameTableController gameTableController) {
        this.gameTableController = gameTableController;
    }

    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }

    public PlayersTableController getPlayersTableController() {
        return playersTableController;
    }
}
