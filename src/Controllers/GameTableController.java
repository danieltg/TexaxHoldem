package Controllers;

import Engine.GameManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class GameTableController implements Initializable {

    @FXML private TextField seat_1;
    @FXML private TextField seat_2;
    @FXML private TextField seat_3;
    @FXML private TextField seat_4;
    @FXML private TextField seat_5;
    @FXML private TextField seat_6;

    private GameManager gameManager;
    private BusinessLogic businessLogic;

    public GameTableController()
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        seat_1.setEditable(false);
        seat_2.setEditable(false);
        seat_3.setEditable(false);
        seat_4.setEditable(false);
        seat_5.setEditable(false);
        seat_6.setEditable(false);

    }

    public void setGameManager(GameManager g) { gameManager=g; }

    public void setBusinessLogic(BusinessLogic b){businessLogic=b;}

    public void clearSeats()
    {
        seat_1.setText("");
        seat_2.setText("");
        seat_3.setText("");
        seat_4.setText("");
        seat_5.setText("");
        seat_6.setText("");

        seat_1.setDisable(true);
        seat_2.setDisable(true);
        seat_3.setDisable(true);
        seat_4.setDisable(true);
        seat_5.setDisable(true);
        seat_6.setDisable(true);

        seat_1.setStyle("-fx-background-color: white;");
        seat_2.setStyle("-fx-background-color: white;");
        seat_3.setStyle("-fx-background-color: white;");
        seat_4.setStyle("-fx-background-color: white;");
        seat_5.setStyle("-fx-background-color: white;");
        seat_6.setStyle("-fx-background-color: white;");

    }
    public void updateGame() {
        clearSeats();
        int numberOfPlayer=gameManager.getPlayers().size();
        switch (numberOfPlayer)
        {
            case 3:
                update3players();
                break;
            case 4:
                update4players();
                break;
            case 5:
                update5players();
                break;
            default:
                update6players();

        }
    }

    public void update3players()
    {
        seat_1.setText(gameManager.getPlayers().get(0).getName());
        seat_1.setDisable(false);
        seat_1.setStyle(gameManager.getPlayers().get(0).getStyle());

        seat_3.setText(gameManager.getPlayers().get(1).getName());
        seat_3.setDisable(false);
        seat_3.setStyle(gameManager.getPlayers().get(1).getStyle());

        seat_5.setText(gameManager.getPlayers().get(2).getName());
        seat_5.setDisable(false);
        seat_5.setStyle(gameManager.getPlayers().get(2).getStyle());


    }

    public void update4players()
    {
        seat_2.setText(gameManager.getPlayers().get(0).getName());
        seat_2.setDisable(false);
        seat_2.setStyle(gameManager.getPlayers().get(0).getStyle());

        seat_3.setText(gameManager.getPlayers().get(1).getName());
        seat_3.setDisable(false);
        seat_3.setStyle(gameManager.getPlayers().get(1).getStyle());

        seat_5.setText(gameManager.getPlayers().get(2).getName());
        seat_5.setDisable(false);
        seat_5.setStyle(gameManager.getPlayers().get(2).getStyle());

        seat_6.setText(gameManager.getPlayers().get(3).getName());
        seat_6.setDisable(false);
        seat_6.setStyle(gameManager.getPlayers().get(3).getStyle());

    }

    public void update5players()
    {
        seat_1.setText(gameManager.getPlayers().get(0).getName());
        seat_2.setStyle(gameManager.getPlayers().get(0).getStyle());

        seat_2.setText(gameManager.getPlayers().get(1).getName());
        seat_2.setStyle(gameManager.getPlayers().get(1).getStyle());

        seat_3.setText(gameManager.getPlayers().get(2).getName());
        seat_3.setStyle(gameManager.getPlayers().get(2).getStyle());

        seat_5.setText(gameManager.getPlayers().get(3).getName());
        seat_5.setStyle(gameManager.getPlayers().get(3).getStyle());

        seat_6.setText(gameManager.getPlayers().get(4).getName());
        seat_6.setStyle(gameManager.getPlayers().get(4).getStyle());


        seat_1.setDisable(false);
        seat_2.setDisable(false);
        seat_3.setDisable(false);
        seat_5.setDisable(false);
        seat_6.setDisable(false);
    }

    public void update6players()
    {
        seat_1.setText(gameManager.getPlayers().get(0).getName());
        seat_1.setStyle(gameManager.getPlayers().get(0).getStyle());

        seat_2.setText(gameManager.getPlayers().get(1).getName());
        seat_2.setStyle(gameManager.getPlayers().get(1).getStyle());

        seat_3.setText(gameManager.getPlayers().get(2).getName());
        seat_3.setStyle(gameManager.getPlayers().get(2).getStyle());

        seat_4.setText(gameManager.getPlayers().get(3).getName());
        seat_4.setStyle(gameManager.getPlayers().get(3).getStyle());

        seat_5.setText(gameManager.getPlayers().get(4).getName());
        seat_5.setStyle(gameManager.getPlayers().get(4).getStyle());

        seat_6.setText(gameManager.getPlayers().get(5).getName());
        seat_6.setStyle(gameManager.getPlayers().get(5).getStyle());


        seat_1.setDisable(false);
        seat_2.setDisable(false);
        seat_3.setDisable(false);
        seat_4.setDisable(false);
        seat_5.setDisable(false);
        seat_6.setDisable(false);
    }

}
