package Controllers;

import javafx.scene.paint.Color;

public class BusinessLogic {

    private MainScreenController controller;

    public BusinessLogic(MainScreenController mainScreenController) {
        controller=mainScreenController;
    }

    public void updatePlayersList()
    {
        controller.updatePlayersTable();
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
        controller.RunOneHand();
    }

    public void changeColor(Color value) {
        controller.updateColor(value);
    }

    public void updateGUIwithStep(int step) {
        controller.updatePlayersTableFromStep(step);
        controller.updateTableCardsFromStep(step);
        controller.updatePlayersOnTable(step);
        controller.updatePotFromStep(step);
        controller.updateStepLabel(step);
    }


    public void updateGUIPotAndPlayerBetAndChips()
    {
        controller.updateGUIPotAndPlayerBetAndChips();
    }
}
