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
    private PokerHand currHand;

    public UIManager() {
        this.gameEnded = false;
        this.mainMenu = new MainMenu();
    }

    private void ReadSettingsXML() throws Exception {

        if (gameManager.GetStateOfGame() !=CurrGameState.Started && gameManager.GetStateOfGame()!=CurrGameState.RunningHand) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a file name: ");
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
            Scanner scanner = new Scanner(System.in);
            System.out.println("What Game file would you like to load? Please provide full path");
            System.out.flush();
            String filename = scanner.nextLine();
            gameManager=SaveGameState.loadGameDataFromFile(filename);
            System.out.println("Game file loaded successfully...");
        } catch (Exception e) {
            System.out.println("Failed to load game from file. Please check your file name");
        }
    }

    private void SaveGame() {
        if(gameManager.GetStateOfGame() ==CurrGameState.NotInitialized)
            System.out.println("Game not initialized yet... nothing to save");
        else
        {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Where would you like to save the file (Please give full path include file extension)?");
                System.out.flush();
                String filename = scanner.nextLine();
                SaveGameState.saveGameDataToFile(filename,gameManager);
            } catch (IOException e) {
                System.out.println("Failed to save Game data");
            }

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
                currHand.addToPot(gameManager.getMoneyFromLastHand());

                List<Winner> winners= run(currHand);

                //inc the handsWon for each winner
                for (Winner w: winners) {
                  w.getPlayer().isAWinner();
                  int chipsToAdd=currHand.getPot()/winners.size();
                  w.getPlayer().addChips(chipsToAdd);
                  System.out.println("Player with ID: "+w.getPlayer().getId() +
                          " won with this hand: "+w.getHandRank() +
                          " .Prize: "+(currHand.getPot()/winners.size()) +"$");
                }

                //Print the game statistic at the end of hand
                gameManager.getStatistics();

                gameManager.setRoles(gameManager.getdealerIndex()+1);
                gameManager.setMoneyFromLastHand(currHand.getPot()%winners.size());
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
        currHand.betSmallAndBig();
        currHand.updateMaxBet();

        collectBets();
        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        notifyUser(1);
        currHand.dealingFlopCards();

        collectBets();
        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        notifyUser(2);
        currHand.dealingTurnCard();

        collectBets();
        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        notifyUser(3);
        currHand.dealingRiverCard();
        collectBets();
        pressAnyKeyToContinue("Hand finished. Press any key to see who is the winner");
        return currHand.evaluateRound();

    }

    private void notifyUser(int round) {

        int currIndex = (1 + currHand.getDealer()) % currHand.getNumberOfPlayers();
        currHand.getPlayers().get(currIndex).itIsMyTurn();

        System.out.println("Here is the hand state after "+round +" round(s) of bet");
        GameStateBoard.printHandState(currHand.getPlayers(),currHand.printHand());
        pressAnyKeyToContinue("Press any key to continue the next bet round...");

    }

    private void pressAnyKeyToContinue(String str)
    {
        System.out.println(str);
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
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
            currPlayer.itIsMyTurn();

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
                } else {
                    //He doesn't have money anymore so we don't want him in this hand
                    currPlayer.setFolded(true);
                    break;
                }
            }

            checkOccurred = true;
            p++;
            currPlayer.itIsNotMyTurn();

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
                currHand.subFromPot(p.getBet());
                p.addChips(p.getBet());


                if (p.getChips() < currHand.getCurrentBet()) {

                    //for check only
                    System.out.println("You dont have enough");
                }
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
                        if (p.getType() == PlayerType.Human) {
                            Scanner scanner = new Scanner(System.in);
                            System.out.print("What would you like to raise to? ");
                            raiseTo = Integer.parseInt(scanner.nextLine());

                        } else
                            raiseTo = p.getRaise(1,currHand.getMaxBet());
                    } catch (NumberFormatException e) {
                        System.out.println("Your input is not valid. Raise must be a number between 1 - " +currHand.getMaxBet()+
                                ", please try again.");
                        raiseTo = 0;
                        continue;
                    }

                        if ( raiseTo > currHand.getMaxBet())
                        {
                            if (p.getType() == PlayerType.Human) {
                                System.out.println("Your input is not valid. Raise must be a number between 1 - " +currHand.getMaxBet()+
                                        ", please try again.");
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
