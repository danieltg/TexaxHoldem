package UI;

import Engine.*;
import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.ReadGameDescriptorFile;

import Engine.Players.Player;
import Engine.Players.PlayerType;
import UI.Boards.GameStateBoard;
import UI.Menus.HandMenu;
import UI.Menus.MainMenu;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UIManager {

    private boolean gameEnded;

    private MainMenu mainMenu;
    private GameManager gameManager= new GameManager();
    private GameStateBoard board;
    private PokerHand currHand;

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
        try {
            gameManager=SaveGameState.loadGameDataFromFile("gameFile.data");
        } catch (IOException e) {
            System.out.println("File not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid file- Game Manager class not found in the file");
        }
    }


    private void SaveGame() {
        try {
            SaveGameState.saveGameDataToFile("gameFile.data",gameManager);
        } catch (IOException e) {
            System.out.println("Failed to save Game data");
        }
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
            if(GameManager.handNumber <gameManager.getGameDescriptor().getStructure().getHandsCount())
            {
                GameManager.handNumber++;
                //create and initiazlized poker hand
                currHand= new PokerHand(gameManager.getGameDescriptor().getStructure().getBlindes(),gameManager.getPlayers());
                List<Winner> winners= run(currHand);
               gameManager.setRoles(gameManager.getdealerIndex()+1);
                //inc the handsWon for each winner
                for (Winner w: winners) {
                  w.getPlayer().isAWinner();
                int chipsToAdd=(w.getPot());
                w.getPlayer().addChips(chipsToAdd);
                 }
                //TODO
                //WE SHOULD PRINT IT! SO MABYE WE NEED TO RETURN THE winners TO THE MAIN...
            }
            else
            {
                throw new GameStateException(GameStateException.INVALID_VALUE + ": ran out of hands");

            }


        }

        else throw new GameStateException(GameStateException.INVALID_VALUE+": before you can use this option, game must be running");
    }

    private List<Winner> run(PokerHand currHand) throws Exception {

        currHand.dealingHoleCards();
        currHand. betSmallAndBig();
        //run round
        collectBets();

        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        GameStateBoard.printHandState(currHand.getPlayers(),currHand.printHand());
        currHand.dealingFlopCards();

        collectBets();

        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        GameStateBoard.printHandState(currHand.getPlayers(),currHand.printHand());
        currHand. dealingTurnCard();

        collectBets();

        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        return currHand.evaluateRound();

    }

    private void collectBets() {
        int p;
        int currIndex;
        boolean checkOccurred = false;

        if (currHand.getRound()==0) {
            currHand.setLastRaise(currHand.getPlayers().get((2 + currHand.getDealer()) %currHand.getNumberOfPlayers()));
            p = 3;
           currHand.setCurrentBet(currHand.getBlinde().getBig());
        }
        else {
            p = 1;
            currHand.setLastRaise(currHand.getPlayers().get((p + currHand.getDealer()) %currHand.getNumberOfPlayers()));
           currHand.setCurrentBet(0);
        }

        while (true) {
            if (currHand.playersLeft() == 1)
                break;

            currIndex = (p + currHand.getDealer()) % currHand.getNumberOfPlayers();
            Player currPlayer =currHand.getPlayers().get(currIndex);

            if (currHand.getLastRaise() != currPlayer || !checkOccurred) {
                if (!currPlayer.isFolded() && currPlayer.getChips() > 0) {
                    if(currPlayer.getType()== PlayerType.Human)
                        GameStateBoard.printHandState(currHand.getPlayers(),currHand.printHand());

                  doThis(nowPlay(currPlayer),currPlayer);
                  if(currPlayer.getType()==PlayerType.Human&&currPlayer.isFolded())
                      return;

                   currHand.addToPot(currPlayer.getBet());
                    currPlayer.collectBet();
                    currHand.updateMaxBet();
                } else
                    break;
            }

            checkOccurred = true;
            p++;

        }
        currHand.upRound();
       currHand.resetPlayersBets();
        currHand.setLastRaise(null);

    }

    private String nowPlay(Player currPlayer) {

        if (currPlayer.getType()==PlayerType.Computer)
           return currPlayer.play();
        else {
            return getUSerSelection();
        }

    }
   private String getUSerSelection()
   {
       boolean validSelection=false;
       String selection="";

       while (!validSelection)
       {
           HandMenu.print();
           try {
               selection= HandMenu.getOptionFromUser();
               validSelection=true;
           }
           catch (Exception e)
           {
               System.out.println(e.getMessage());
           }

       }

       return selection;

   }
    private void doThis(String whatToDo, Player p) {

        while (true) {

            if (whatToDo.equals("F")) {
                p.setFolded(true);

                break;
            }
            if (whatToDo.equals("B")) {
                int betTO = 0;
                try {
                    if (currHand.getLastRaise() == null) {

                        Scanner scanner = new Scanner(System.in);
                        System.out.print("What would you like to Bet to? ");
                        betTO = Integer.parseInt(scanner.nextLine());

                        if (betTO <= currHand.getCurrentBet() || betTO > p.getChips() || betTO > currHand.getMaxBet()) {
                            System.out.println("Please enter a valid bet: ");
                            betTO = 0;
                        }

                        currHand.setCurrentBet(betTO);
                        currHand.setLastRaise(p);
                        p.setBet(currHand.getCurrentBet());
                        break;

                    }
                    else {
                        System.out.println("You can't Bet now ,please choose other option");
                       whatToDo= getUSerSelection();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid bet: ");
                    betTO = 0;
                    continue; }



            }

            if (whatToDo.equals("C")) {
                p.addChips(p.getBet());
                currHand.subFromPot(p.getBet());

                if (p.getChips() < currHand.getCurrentBet())
                    p.setBet(p.getChips());
                else
                    p.setBet(currHand.getCurrentBet());

                break;

            }

            if (whatToDo.equals("R")) {
                currHand.subFromPot(p.getBet());
                p.addChips(p.getBet());

                int raiseTo = 0;
                while (raiseTo == 0) {
                    try {
                        //TODO:I'm not sure the min and mac are correct
                        if (p.getType() == PlayerType.Human) {
                            Scanner scanner = new Scanner(System.in);
                            System.out.print("What would you like to raise to? ");
                            raiseTo = Integer.parseInt(scanner.nextLine());
                        } else
                            raiseTo = p.getRaise(1,currHand.getMaxBet());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid raise: ");
                        raiseTo = 0;
                        continue;
                    }

                        if ( raiseTo > currHand.getMaxBet())
                        {
                            if (p.getType() == PlayerType.Human) {
                                System.out.println("Please enter a valid raise: ");
                            }
                            else
                            {
                                whatToDo=p.play();
                            }
                            raiseTo = 0;
                        }

                }

                    currHand.setCurrentBet(currHand.getCurrentBet()+ raiseTo);
                    currHand.setLastRaise(p);
                    p.setBet(currHand.getCurrentBet());
                break;

            }
            if (whatToDo.equals("K")) {
                if(p.getBet()==currHand.getCurrentBet())
                {
                    currHand.setLastRaise(p);
                    p.setBet(currHand.getCurrentBet());
                    break;
                }
                else
                {

                    if(p.getType()==PlayerType.Human) {
                        System.out.println("You can't Check now ,please choose other option");
                    whatToDo=getUSerSelection();
                    }
                    else
                       whatToDo=p.play();
                }

            }

        }
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
