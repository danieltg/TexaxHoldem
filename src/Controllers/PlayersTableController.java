package Controllers;

import Engine.GameManager;
import Engine.Players.PokerPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.text.TabableView;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayersTableController implements Initializable {

    @FXML private TableView<PokerPlayer> playersTableView;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn typeColumn;
    @FXML private TableColumn buysColumn;
    @FXML private TableColumn handsWinsColumn;
    @FXML private TableColumn winnigPriceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("type"));
        buysColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("numbersOfBuy"));
        handsWinsColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("handsWon"));
        winnigPriceColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("chips"));

    }


    public void updatePlayersTable(ObservableList<PokerPlayer> pokerPlayers)
    {
        for ( int i = 0; i<playersTableView.getItems().size(); i++) {
            playersTableView.getItems().clear();
        }
        playersTableView.getItems().addAll(pokerPlayers);
    }


}
