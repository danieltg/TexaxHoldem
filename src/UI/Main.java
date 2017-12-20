package UI;

import Controllers.MainScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.View;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/MainScreen.fxml"));


        primaryStage.setTitle("Texas Holdem Poker");
        primaryStage.setScene(new Scene(root, 1500, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
