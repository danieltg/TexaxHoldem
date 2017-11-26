package UI;

import Engine.CurrGameState;
import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;
import Engine.Players.ComputerPlayer;
import Engine.Players.HumanPlayer;
import Engine.Players.Player;
import UI.Boards.GameStateBoard;
import UI.Menus.MainMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UIManager {

    private boolean gameEnded;

    private MainMenu mainMenu;
    private GameManager gameManager= new GameManager();
    private GameStateBoard board;

    public UIManager() {
        this.gameEnded = false;
        this.mainMenu = new MainMenu();
    }

    private void ReadSettingsXML() throws Exception {

        if (gameManager.GetStateOfGame() == CurrGameState.NotInitialized||gameManager.GetStateOfGame()==CurrGameState.Ended) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a file name:");
                System.out.flush();
                String filename = scanner.nextLine();
                gameManager.setGameDescriptor(ReadGameDescriptorFile.readFile(filename));
                gameManager.setStateOfGame(CurrGameState.Initialized);


            } catch (Exception e) {
                throw new Exception("Invalid Game Descriptor file: " + e.getMessage() + ".");
            }
        }
        else{
            throw new GameStateException(GameStateException.INVALID_VALUE +": Configuration file already loaded.");
        }
    }


    public void run() throws InterruptedException {
        int selectedMainOption=6;
        boolean shouldContinue=true;

        while (shouldContinue || !gameEnded) {
            try {
                mainMenu.Print();
                selectedMainOption = mainMenu.getOptionFromUser();
                shouldContinue=false;
                //we have a correct input
                runOption(selectedMainOption);

            } catch (NumberFormatException ne) {
                System.out.println("***Your input is not a valid option , please try again.");
                TimeUnit.MILLISECONDS.sleep(500);
            }

        }

    }
    private void runOption(int selectedMainOption) {
        switch (selectedMainOption) {
            case 1:
                try {
                    ReadSettingsXML();
                    System.out.println("Configuration file loaded successfully");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                try {
                    StartGame();
                } catch (GameStateException e) {
                   System.out.println(e.getMessage());
                }
                break;
            case 3:
            {
                try {
                    showGameState();
                } catch (GameStateException e) {
                    System.out.println(e.getMessage());
                }

            }

                break;
            case 4: RunOneHand();break;
            case 5: GetStatistics();break;
            case 6: LeaveTheGame();break;
            case 7: SaveGame();break;
            case 8: LoadGame();break;



        }
    }

    private void LoadGame() {
    }


    private void SaveGame() {
    }

    private void LeaveTheGame() {
        System.out.println("Thanks for using Texas Hold'em.");
        System.exit(0);
    }

    private void GetStatistics() {
    }

    private void RunOneHand() {
        gameManager.runHand();
    }

    private void showGameState() throws GameStateException {
        if(gameManager.GetStateOfGame() ==CurrGameState.Started)
            gameManager.printGameState();
        else throw new GameStateException(GameStateException.INVALID_VALUE+": before you can use this option, game must be running");
    }

    private void StartGame() throws GameStateException {

        gameManager.startGame();

    }

}
