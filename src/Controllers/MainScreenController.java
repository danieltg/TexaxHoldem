package Controllers;

import Engine.GameManager;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class MainScreenController {
    private GameManager gameManager;
    private Stage primaryStage;


    public void setStage(Stage PrimaryStage)
    {
        primaryStage = PrimaryStage;
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
}
