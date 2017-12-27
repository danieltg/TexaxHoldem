package Controllers;

import javafx.scene.paint.Color;

public class BusinessLogic {

    private MainScreenController controller;

    public BusinessLogic(MainScreenController mainScreenController) {
        controller=mainScreenController;
    }

    public void updatePlayersList()
    {
        controller.setPlayerTable();
    }

    public void updateGameSettings()
    {
        controller.setGameSettings();
    }

    public void updateUI() {

        updatePlayersList();
        updateGameSettings();
        setGameTable();
    }

    private void setGameTable() {
        controller.setGameTable();
    }

    public void startGame() {
        controller.updateGameDetails();
        controller.updateTableCards();
    }

    public void changeColor(Color value) {
        controller.updateColor(value);
    }
}