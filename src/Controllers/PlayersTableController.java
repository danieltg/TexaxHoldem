package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.swing.text.TabableView;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayersTableController implements Initializable {

    @FXML private TableView playersTableView;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn typeColumn;
    @FXML private TableColumn buysColumn;
    @FXML private TableColumn handsWinsColumn;
    @FXML private TableColumn winnigPriceColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
