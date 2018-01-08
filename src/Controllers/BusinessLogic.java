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

    public void startHand() {
        //init hand
        controller.startNewHand();
        //update UI fields
        controller.updateGameDetails();
        //Runfirsthand
        controller.RunOneHand();

    }

    public void changeColor(Color value) {
        controller.updateColor(value);
    }

    public void updateGUIwithStep(int step) {
        // We don't want to update the table during the Replay
        // controller.updatePlayersTableFromStep(step);
        controller.updateTableCardsFromStep(step);
        controller.updatePlayersOnTable(step);
        controller.updatePotFromStep(step);
        controller.updateStepLabel(step);
        controller.updatePlayersCardsOnTableFromStep(step);
    }


    public void updateGUIPotAndPlayerBetAndChips()
    {
        controller.updateGUIPotAndPlayerBetAndChips();
    }

    public void runNextHand() {
        controller.startNewHand();
        controller.RunOneHand();
    }

    public void updateUserSelection(String action, int info) {

        controller.updateCurrPlayerWithSelection(action,info);
    }

    public void clearAllCardsOnTable()
    {
        controller.clearAllCardsOnTable();
    }

    public void changeToStyle1() {
        controller.setStyle1();
    }

    public void changeToStyle2() {
        controller.setStyle2();
    }

    public void basicStyle()
    {
        controller.removeStyle();
    }

    public void clearGameTable() {
        controller.clearGameTable();
        updatePlayersList();
        setGameTable();
    }

    public void hideGameTable() {
        controller.hideGameTable();
    }

    public boolean isAnimationEnabled() {
        return controller.isAnimationEnabled();
    }

    public void clearOnlyGameTable() {
        controller.clearGameTable();
    }

    public void closeGameInfoAndActions() {
        controller.closeGameInfoAndActions();
    }
}
