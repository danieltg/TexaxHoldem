package UI;

import Engine.CurrGameState;
import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;

import Engine.Winner;
import UI.Boards.GameStateBoard;
import UI.Menus.MainMenu;


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

        if (gameManager.GetStateOfGame() !=CurrGameState.Started && gameManager.GetStateOfGame()!=CurrGameState.RunningHand) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a file name:");
                System.out.flush();
                String filename = scanner.nextLine();
                gameManager.setGameDescriptor(ReadGameDescriptorFile.readFile(filename));
                gameManager.setTable();
                System.out.println("Configuration file loaded successfully...");
                gameManager.printGameState();

            } catch (Exception e) {
                throw new Exception("Invalid Game Descriptor file: " + e.getMessage() + ".");
            }
        }
        else{
            throw new GameStateException(GameStateException.INVALID_VALUE +": Configuration file already loaded.");
        }
    }


    public void run() throws Exception {
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
    private void runOption(int selectedMainOption) throws Exception {
        switch (selectedMainOption) {
            case 1: {
                try
                {
                    ReadSettingsXML();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    runOption(1);
                }
                break;

            }
            case 2: {
                try
                {
                    startGame();
                }
                catch (GameStateException e) { System.out.println(e.getMessage()); }
                break;
            }
            case 3:
            {
                try { showGameState(); }
                catch (GameStateException e) { System.out.println(e.getMessage()); }
                break;
            }

            case 4:
            {
                try{ RunOneHand();}
                catch (GameStateException e) { System.out.println(e.getMessage()); }
                break;
            }
            case 5: {
                try { getStatistics(); }
                catch (GameStateException e) { System.out.println(e.getMessage()); }
                break;
            }
            case 6:{
                try { buy();
                }
                catch (GameStateException e) {System.out.println(e.getMessage());
                }
                break;
            }
            case 7: {
                try{
                leaveTheCurrentGame();}
                catch (GameStateException e) {System.out.println(e.getMessage());
                }
                break;
            }
           case 8: SaveGame();break;
            case 9: LoadGame();break;
            case 10: {
                exitGame();
                break;
            }


        }
    }

    private void exitGame() {
        System.out.println("Thanks for using Texas Hold'em.");
        System.exit(0);
    }

    private void buy() throws GameStateException {
        if(gameManager.GetStateOfGame() ==CurrGameState.Started)
            gameManager.buy();
        else throw new GameStateException(GameStateException.INVALID_VALUE+": before you can use this option, game must be running");
    }

    private void LoadGame() {
    }


    private void SaveGame() {
    }

    private void leaveTheCurrentGame() throws GameStateException {
        if(gameManager.GetStateOfGame()==CurrGameState.Started) {
            System.out.println("Alright, you leaved the game... You can either start a new one or exit the game");
            gameManager.resetGame();
        }
        else throw new GameStateException(GameStateException.INVALID_VALUE+":before you can use this option,game must be running");
    }

    private void getStatistics() throws GameStateException {
        if(gameManager.GetStateOfGame() ==CurrGameState.Started)
            gameManager.getStatistics();
        else throw new GameStateException(GameStateException.INVALID_VALUE+": before you can use this option, game must be running");
    }

    private void RunOneHand() throws Exception {
        if(gameManager.GetStateOfGame() ==CurrGameState.Started)
        {
            List<Winner> winners= gameManager.runHand();

            //inc the handsWon for each winner
            for (Winner w: winners) {
                w.getPlayer().isAWinner();
                int chipsToAdd=(w.getEquity()*w.getPot()/100);
                w.getPlayer().addChips(chipsToAdd);
            }
            //TODO
            //WE SHOULD PRINT IT! SO MABYE WE NEED TO RETURN THE winners TO THE MAIN...
        }

        else throw new GameStateException(GameStateException.INVALID_VALUE+": before you can use this option, game must be running");
    }

    private void showGameState() throws GameStateException {
        if(gameManager.GetStateOfGame() ==CurrGameState.Started||gameManager.GetStateOfGame() ==CurrGameState.Initialized)
            gameManager.printGameState();
        else throw new GameStateException(GameStateException.INVALID_VALUE+": before you can use this option, game must be running");
    }

    private void startGame() throws GameStateException {

        gameManager.startGame();
        System.out.println("Game started successfully...");
    }

}
