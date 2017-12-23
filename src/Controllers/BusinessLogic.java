package Controllers;

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
    }

    public void startGame() {
        updateGameDetails();
    }

    private void updateGameDetails() {
        controller.updateGameDetails();
    }
}
