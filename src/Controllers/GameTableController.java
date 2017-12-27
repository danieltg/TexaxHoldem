package Controllers;

import Engine.GameManager;
import Engine.Players.PokerPlayer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameTableController implements Initializable {

    @FXML private Circle table;


    @FXML private Pane seat1_data;
    @FXML private Pane seat2_data;
    @FXML private Pane seat3_data;
    @FXML private Pane seat4_data;
    @FXML private Pane seat5_data;
    @FXML private Pane seat6_data;

    @FXML private ImageView tCard1;
    @FXML private ImageView tCard2;
    @FXML private ImageView tCard3;
    @FXML private ImageView tCard4;
    @FXML private ImageView tCard5;

    @FXML private Label namePlayer1;
    @FXML private Label chipsPlayer1;
    @FXML private Label betPlayer1;
    @FXML private Label rolePlayer1;

    @FXML private Label namePlayer2;
    @FXML private Label chipsPlayer2;
    @FXML private Label betPlayer2;
    @FXML private Label rolePlayer2;

    @FXML private Label namePlayer3;
    @FXML private Label chipsPlayer3;
    @FXML private Label betPlayer3;
    @FXML private Label rolePlayer3;

    @FXML private Label namePlayer4;
    @FXML private Label chipsPlayer4;
    @FXML private Label betPlayer4;
    @FXML private Label rolePlayer4;

    @FXML private Label namePlayer5;
    @FXML private Label chipsPlayer5;
    @FXML private Label betPlayer5;
    @FXML private Label rolePlayer5;

    @FXML private Label namePlayer6;
    @FXML private Label chipsPlayer6;
    @FXML private Label betPlayer6;
    @FXML private Label rolePlayer6;

    private List<PokerPlayer> players= new ArrayList<>();

    private SimpleStringProperty _namePlayer2;

    private GameManager gameManager;
    private BusinessLogic businessLogic;

    public GameTableController()
    {
        _namePlayer2=new SimpleStringProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        seat1_data.setVisible(false);
        seat2_data.setVisible(false);
        seat3_data.setVisible(false);
        seat4_data.setVisible(false);
        seat5_data.setVisible(false);
        seat6_data.setVisible(false);

        namePlayer2.textProperty().bind(_namePlayer2);


    }

    public void setGameManager(GameManager g) { gameManager=g; }

    public void setBusinessLogic(BusinessLogic b){businessLogic=b;}

    public void updateGame() {

        int numberOfPlayer=gameManager.getPlayers().size();
        players=gameManager.getPlayers();
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

        seat1_data.setVisible(true);
        seat3_data.setVisible(true);
        seat5_data.setVisible(true);

    }

    public void update4players()
    {

        seat2_data.setVisible(true);
        _namePlayer2.setValue(players.get(0).getName());
        seat3_data.setVisible(true);
        seat5_data.setVisible(true);
        seat6_data.setVisible(true);

    }

    public void update5players()
    {
        seat1_data.setVisible(true);
        seat2_data.setVisible(true);
        seat3_data.setVisible(true);
        seat5_data.setVisible(true);
        seat6_data.setVisible(true);
    }

    public void update6players()
    {

        seat1_data.setVisible(true);
        seat2_data.setVisible(true);
        seat3_data.setVisible(true);
        seat4_data.setVisible(true);
        seat5_data.setVisible(true);
        seat6_data.setVisible(true);
    }

    public void updateCards()
    {
        String[] tableCards=gameManager.getCurrHand().getCardsAsStringArray();

        String BASE_PACKAGE="/Resources/cards/";

        tCard1.setImage(new Image(BASE_PACKAGE+(tableCards[0].equals("??")?"back":tableCards[0])+".png"));
        tCard2.setImage(new Image(BASE_PACKAGE+(tableCards[1].equals("??")?"back":tableCards[1])+".png"));
        tCard3.setImage(new Image(BASE_PACKAGE+(tableCards[2].equals("??")?"back":tableCards[2])+".png"));
        tCard4.setImage(new Image(BASE_PACKAGE+(tableCards[3].equals("??")?"back":tableCards[3])+".png"));
        tCard5.setImage(new Image(BASE_PACKAGE+(tableCards[4].equals("??")?"back":tableCards[4])+".png"));

    }

    public void changeTableColor(Color value) {
        table.setFill(value);
    }


}
