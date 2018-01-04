package UI;

import Controllers.BusinessLogic;
import Controllers.MainScreenController;
import Engine.GameManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

class TexasBuilder {

    private GameManager gameManager;
    private Stage primaryStage;

    public TexasBuilder(Stage PrimaryStage)
    {
        gameManager=new GameManager();
        primaryStage=PrimaryStage;
    }

    public void execute() throws IOException
    {
        Parent root = loadView();
        setPrimaryStage(root);
    }

    private Parent loadView() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/Resources/View/MainScreen.fxml");
        fxmlLoader.setLocation(url);
        Parent view = fxmlLoader.load(url.openStream());

        MainScreenController mainScreenController=fxmlLoader.getController();
        BusinessLogic businessLogic = new BusinessLogic(mainScreenController);

        mainScreenController.setPrimaryStage(primaryStage);
        mainScreenController.setBusinessLogic(businessLogic);
        return view;
    }

    private void setPrimaryStage(Parent i_root)
    {
        Scene scene = new Scene(i_root, 1100, 800);

      //  scene.getStylesheets().add("/resources/Style/Default.css");
        primaryStage.setTitle("Texas Holdem");
        primaryStage.setScene(scene);
    }
}
