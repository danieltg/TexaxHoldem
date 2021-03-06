package Controllers;

import Engine.Players.PokerPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayersTableController implements Initializable {

    @FXML private TableColumn chipsColumn;
    @FXML private TableView<PokerPlayer> playersTableView;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn typeColumn;
    @FXML private TableColumn buysColumn;
    @FXML private TableColumn handsWinsColumn;
    @FXML private TableColumn winnigPriceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playersTableView.setStyle("-fx-selection-bar: green; -fx-selection-bar-non-focused: salmon;");

        nameColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("type"));
        buysColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("numbersOfBuy"));
        handsWinsColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("handsWon"));
        chipsColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("chips"));
        winnigPriceColumn.setCellValueFactory(new PropertyValueFactory<PokerPlayer, String>("winnigPrice"));

    }


    public void updatePlayersTable(ObservableList<PokerPlayer> pokerPlayers)
    {
        for ( int i = 0; i<playersTableView.getItems().size(); i++) {
            playersTableView.getItems().clear();
        }
        playersTableView.getItems().addAll(pokerPlayers);
    }

    public void updateTableWithWinner(int ID) {

        playersTableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        for ( int i = 0; i<playersTableView.getItems().size(); i++) {
            if(playersTableView.getItems().get(i).getId()==ID)
            {
                playersTableView.getSelectionModel().select(i);
            }
        }

        chipsColumn.setSortType(TableColumn.SortType.DESCENDING);
        playersTableView.getSortOrder().add(chipsColumn);

    }
}
