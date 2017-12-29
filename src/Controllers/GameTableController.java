package Controllers;

import Engine.GameManager;
import Engine.Players.PokerPlayer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static Engine.Utils.EngineUtils.BASE_PACKAGE;

public class GameTableController implements Initializable {

    @FXML private Circle table;

    @FXML private Label potLabel;


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


    private SimpleStringProperty _namePlayer1;
    private SimpleStringProperty _namePlayer2;
    private SimpleStringProperty _namePlayer3;
    private SimpleStringProperty _namePlayer4;
    private SimpleStringProperty _namePlayer5;
    private SimpleStringProperty _namePlayer6;

    private SimpleStringProperty _rolePlayer1;
    private SimpleStringProperty _rolePlayer2;
    private SimpleStringProperty _rolePlayer3;
    private SimpleStringProperty _rolePlayer4;
    private SimpleStringProperty _rolePlayer5;
    private SimpleStringProperty _rolePlayer6;

    private SimpleIntegerProperty _chipsPlayer1;
    private SimpleIntegerProperty _chipsPlayer2;
    private SimpleIntegerProperty _chipsPlayer3;
    private SimpleIntegerProperty _chipsPlayer4;
    private SimpleIntegerProperty _chipsPlayer5;
    private SimpleIntegerProperty _chipsPlayer6;

    private SimpleIntegerProperty _betPlayer1;
    private SimpleIntegerProperty _betPlayer2;
    private SimpleIntegerProperty _betPlayer3;
    private SimpleIntegerProperty _betPlayer4;
    private SimpleIntegerProperty _betPlayer5;
    private SimpleIntegerProperty _betPlayer6;

    private SimpleIntegerProperty _pot;

    private GameManager gameManager;
    private BusinessLogic businessLogic;

    public GameTableController()
    {
        _pot=new SimpleIntegerProperty(0);

        _namePlayer1=new SimpleStringProperty();
        _namePlayer2=new SimpleStringProperty();
        _namePlayer3=new SimpleStringProperty();
        _namePlayer4=new SimpleStringProperty();
        _namePlayer5=new SimpleStringProperty();
        _namePlayer6=new SimpleStringProperty();

        _rolePlayer1=new SimpleStringProperty();
        _rolePlayer2=new SimpleStringProperty();
        _rolePlayer3=new SimpleStringProperty();
        _rolePlayer4=new SimpleStringProperty();
        _rolePlayer5=new SimpleStringProperty();
        _rolePlayer6=new SimpleStringProperty();

        _betPlayer1=new SimpleIntegerProperty();
        _betPlayer2=new SimpleIntegerProperty();
        _betPlayer3=new SimpleIntegerProperty();
        _betPlayer4=new SimpleIntegerProperty();
        _betPlayer5=new SimpleIntegerProperty();
        _betPlayer6=new SimpleIntegerProperty();

        _chipsPlayer1=new SimpleIntegerProperty();
        _chipsPlayer2= new SimpleIntegerProperty();
        _chipsPlayer3=new SimpleIntegerProperty();
        _chipsPlayer4=new SimpleIntegerProperty();
        _chipsPlayer5=new SimpleIntegerProperty();
        _chipsPlayer6=new SimpleIntegerProperty();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        potLabel.textProperty().bind(_pot.asString());

        seat1_data.setVisible(false);
        seat2_data.setVisible(false);
        seat3_data.setVisible(false);
        seat4_data.setVisible(false);
        seat5_data.setVisible(false);
        seat6_data.setVisible(false);

        namePlayer1.textProperty().bind(_namePlayer1);
        namePlayer2.textProperty().bind(_namePlayer2);
        namePlayer3.textProperty().bind(_namePlayer3);
        namePlayer4.textProperty().bind(_namePlayer4);
        namePlayer5.textProperty().bind(_namePlayer5);
        namePlayer6.textProperty().bind(_namePlayer6);

        rolePlayer1.textProperty().bind(_rolePlayer1);
        rolePlayer2.textProperty().bind(_rolePlayer2);
        rolePlayer3.textProperty().bind(_rolePlayer3);
        rolePlayer4.textProperty().bind(_rolePlayer4);
        rolePlayer5.textProperty().bind(_rolePlayer5);
        rolePlayer6.textProperty().bind(_rolePlayer6);

        chipsPlayer1.textProperty().bind(_chipsPlayer1.asString());
        chipsPlayer2.textProperty().bind(_chipsPlayer2.asString());
        chipsPlayer3.textProperty().bind(_chipsPlayer3.asString());
        chipsPlayer4.textProperty().bind(_chipsPlayer4.asString());
        chipsPlayer5.textProperty().bind(_chipsPlayer5.asString());
        chipsPlayer6.textProperty().bind(_chipsPlayer6.asString());

        betPlayer1.textProperty().bind(_betPlayer1.asString());
        betPlayer2.textProperty().bind(_betPlayer2.asString());
        betPlayer3.textProperty().bind(_betPlayer3.asString());
        betPlayer4.textProperty().bind(_betPlayer4.asString());
        betPlayer5.textProperty().bind(_betPlayer5.asString());
        betPlayer6.textProperty().bind(_betPlayer6.asString());
    }

    public void setGameManager(GameManager g) { gameManager=g; }

    public void setBusinessLogic(BusinessLogic b){businessLogic=b;}

    public void updatePlayersOnTable(List<PokerPlayer> playerList) {
        clearTable();
        int numberOfPlayer=playerList.size();

        switch (numberOfPlayer)
        {
            case 3:
                update3players(playerList);
                break;
            case 4:
                update4players(playerList);
                break;
            case 5:
                update5players(playerList);
                break;
            default:
                update6players(playerList);

        }
    }

    private void clearTable() {

        seat1_data.setVisible(false);
        seat2_data.setVisible(false);
        seat3_data.setVisible(false);
        seat4_data.setVisible(false);
        seat5_data.setVisible(false);
        seat6_data.setVisible(false);
    }


    public void update3players(List<PokerPlayer> playerList)
    {
        seat1_data.setVisible(true);
        seat1_data.setStyle(playerList.get(0).getStyle());
        _namePlayer1.setValue(playerList.get(0).getName());
        _rolePlayer1.setValue(playerList.get(0).getState().name());

        seat3_data.setVisible(true);
        seat3_data.setStyle(playerList.get(1).getStyle());
        _namePlayer3.setValue(playerList.get(1).getName());
        _rolePlayer3.setValue(playerList.get(1).getState().name());

        seat5_data.setVisible(true);
        seat5_data.setStyle(playerList.get(2).getStyle());
        _namePlayer5.setValue(playerList.get(2).getName());
        _rolePlayer5.setValue(playerList.get(2).getState().name());

        update3playersChipsAndBet(playerList);
    }

    private void update3playersChipsAndBet(List<PokerPlayer> playerList) {
        _chipsPlayer1.setValue(playerList.get(0).getChips());
        _betPlayer1.setValue(playerList.get(0).getBet());

        _chipsPlayer3.setValue(playerList.get(1).getChips());
        _betPlayer3.setValue(playerList.get(1).getBet());

        _chipsPlayer5.setValue(playerList.get(2).getChips());
        _betPlayer5.setValue(playerList.get(2).getBet());
    }

    public void update4players(List<PokerPlayer> playerList)
    {

        seat2_data.setVisible(true);
        seat2_data.setStyle(playerList.get(0).getStyle());
        _namePlayer2.setValue(playerList.get(0).getName());
        _rolePlayer2.setValue(playerList.get(0).getState().name());


        seat3_data.setVisible(true);
        seat3_data.setStyle(playerList.get(1).getStyle());
        _namePlayer3.setValue(playerList.get(1).getName());
        _rolePlayer3.setValue(playerList.get(1).getState().name());


        seat5_data.setVisible(true);
        seat5_data.setStyle(playerList.get(2).getStyle());
        _namePlayer5.setValue(playerList.get(2).getName());
        _rolePlayer5.setValue(playerList.get(2).getState().name());


        seat6_data.setVisible(true);
        seat6_data.setStyle(playerList.get(3).getStyle());
        _namePlayer6.setValue(playerList.get(3).getName());
        _rolePlayer6.setValue(playerList.get(3).getState().name());


        update4playersChipsAndBet(playerList);
    }

    private void update4playersChipsAndBet(List<PokerPlayer> playerList) {

        _chipsPlayer2.setValue(playerList.get(0).getChips());
        _betPlayer2.setValue(playerList.get(0).getBet());

        _chipsPlayer3.setValue(playerList.get(1).getChips());
        _betPlayer3.setValue(playerList.get(1).getBet());

        _chipsPlayer5.setValue(playerList.get(2).getChips());
        _betPlayer5.setValue(playerList.get(2).getBet());

        _chipsPlayer6.setValue(playerList.get(3).getChips());
        _betPlayer6.setValue(playerList.get(3).getBet());
    }

    public void update5players(List<PokerPlayer> playerList)
    {
        seat1_data.setVisible(true);
        seat1_data.setStyle(playerList.get(0).getStyle());
        _namePlayer1.setValue(playerList.get(0).getName());
        _rolePlayer1.setValue(playerList.get(0).getState().name());

        seat2_data.setVisible(true);
        seat2_data.setStyle(playerList.get(1).getStyle());
        _namePlayer2.setValue(playerList.get(1).getName());
        _rolePlayer2.setValue(playerList.get(1).getState().name());

        seat3_data.setVisible(true);
        seat3_data.setStyle(playerList.get(2).getStyle());
        _namePlayer3.setValue(playerList.get(2).getName());
        _rolePlayer3.setValue(playerList.get(2).getState().name());

        seat5_data.setVisible(true);
        seat5_data.setStyle(playerList.get(3).getStyle());
        _namePlayer5.setValue(playerList.get(3).getName());
        _rolePlayer5.setValue(playerList.get(3).getState().name());

        seat6_data.setVisible(true);
        seat6_data.setStyle(playerList.get(4).getStyle());
        _namePlayer6.setValue(playerList.get(4).getName());
        _rolePlayer6.setValue(playerList.get(4).getState().name());

        update5playersChipsAndBet(playerList);
    }

    private void update5playersChipsAndBet(List<PokerPlayer> playerList) {

        _chipsPlayer1.setValue(playerList.get(0).getChips());
        _betPlayer1.setValue(playerList.get(0).getBet());

        _chipsPlayer2.setValue(playerList.get(1).getChips());
        _betPlayer2.setValue(playerList.get(1).getBet());

        _chipsPlayer3.setValue(playerList.get(2).getChips());
        _betPlayer3.setValue(playerList.get(2).getBet());

        _chipsPlayer5.setValue(playerList.get(3).getChips());
        _betPlayer5.setValue(playerList.get(3).getBet());

        _chipsPlayer6.setValue(playerList.get(4).getChips());
        _betPlayer6.setValue(playerList.get(4).getBet());
    }


    public void update6players(List<PokerPlayer> playerList)
    {

        seat1_data.setVisible(true);
        seat1_data.setStyle(playerList.get(0).getStyle());
        _namePlayer1.setValue(playerList.get(0).getName());
        _rolePlayer1.setValue(playerList.get(0).getState().name());

        seat2_data.setVisible(true);
        seat2_data.setStyle(playerList.get(1).getStyle());
        _namePlayer2.setValue(playerList.get(1).getName());
        _rolePlayer2.setValue(playerList.get(1).getState().name());

        seat3_data.setVisible(true);
        seat3_data.setStyle(playerList.get(2).getStyle());
        _namePlayer3.setValue(playerList.get(2).getName());
        _rolePlayer3.setValue(playerList.get(2).getState().name());

        seat4_data.setVisible(true);
        seat4_data.setStyle(playerList.get(3).getStyle());
        _namePlayer4.setValue(playerList.get(3).getName());
        _rolePlayer4.setValue(playerList.get(3).getState().name());

        seat5_data.setVisible(true);
        seat5_data.setStyle(playerList.get(4).getStyle());
        _namePlayer5.setValue(playerList.get(4).getName());
        _rolePlayer5.setValue(playerList.get(4).getState().name());

        seat6_data.setVisible(true);
        seat6_data.setStyle(playerList.get(5).getStyle());
        _namePlayer6.setValue(playerList.get(5).getName());
        _rolePlayer6.setValue(playerList.get(5).getState().name());

        update6playersChipsAndBet(playerList);
    }

    private void update6playersChipsAndBet(List<PokerPlayer> playerList) {

        _chipsPlayer1.setValue(playerList.get(0).getChips());
        _betPlayer1.setValue(playerList.get(0).getBet());

        _chipsPlayer2.setValue(playerList.get(1).getChips());
        _betPlayer2.setValue(playerList.get(1).getBet());

        _chipsPlayer3.setValue(playerList.get(2).getChips());
        _betPlayer3.setValue(playerList.get(2).getBet());

        _chipsPlayer4.setValue(playerList.get(3).getChips());
        _betPlayer4.setValue(playerList.get(3).getBet());

        _chipsPlayer5.setValue(playerList.get(4).getChips());
        _betPlayer5.setValue(playerList.get(4).getBet());

        _chipsPlayer6.setValue(playerList.get(5).getChips());
        _betPlayer6.setValue(playerList.get(5).getBet());
    }

    public void updateCards(String[] tableCards)
    {
        tCard1.setImage(new Image(BASE_PACKAGE+(tableCards[0].equals("??")?"back":tableCards[0])+".png"));
        tCard2.setImage(new Image(BASE_PACKAGE+(tableCards[1].equals("??")?"back":tableCards[1])+".png"));
        tCard3.setImage(new Image(BASE_PACKAGE+(tableCards[2].equals("??")?"back":tableCards[2])+".png"));
        tCard4.setImage(new Image(BASE_PACKAGE+(tableCards[3].equals("??")?"back":tableCards[3])+".png"));
        tCard5.setImage(new Image(BASE_PACKAGE+(tableCards[4].equals("??")?"back":tableCards[4])+".png"));

    }

    public void changeTableColor(Color value) {
        table.setFill(value);
    }

    public void updatePot(int i)
    {
        _pot.setValue(i);
    }

    public void updatePlayersBetAndChips(List<PokerPlayer> playerList) {

        int numberOfPlayer=playerList.size();

        switch (numberOfPlayer)
        {
            case 3:
                update3playersChipsAndBet(playerList);
                break;
            case 4:
                update4playersChipsAndBet(playerList);
                break;
            case 5:
                update5playersChipsAndBet(playerList);
                break;
            default:
                update6playersChipsAndBet(playerList);

        }
    }
}
