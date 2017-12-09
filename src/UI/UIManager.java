package UI;

import Engine.*;
import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.Blindes;
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
        if(gameManager.GetStateOfGame() ==CurrGameState.Started) {
            gameManager.buy();
            System.out.println("Buy operation completed successfully");
        }
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
            if (!gameManager.doesBigAndSallPlayersHaveMoney()) {
                System.out.println("We're sorry but at least one of the player (Big or Small) does not have enough money to start the game.\n"
                        + "We need " + gameManager.getBig() + "$ for Big and " + gameManager.getSmall() + "$ for Small");
                gameManager.printGameState();
                return;
            }

            if (!gameManager.doesHumanPlayersHaveMoney()) {
                System.out.println("We're sorry but the Human player does not have enough money to start the game.\n"
                        + "You can select 6 in the main menu and get more money $$$");
                gameManager.printGameState();
                return;
            }

            if(GameManager.handNumber <gameManager.getGameDescriptor().getStructure().getHandsCount())
            {
                GameManager.handNumber++;
                Blindes blindes=gameManager.getGameDescriptor().getStructure().getBlindes();

                currHand= new PokerHand(blindes,gameManager.getPlayers());
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

        currHand.upRound();
        currHand.setLastRaise(null);
        notifyUser(1);
        currHand.dealingFlopCards();

        collectBets();
        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        currHand.upRound();
        currHand.setLastRaise(null);
        notifyUser(2);
        currHand.dealingTurnCard();

        collectBets();
        if (currHand.playersLeft() == 1||currHand.humanIsLeft())
            return currHand.getWinner();

        currHand.upRound();
        notifyUser(3);
        currHand.setLastRaise(null);
        currHand.dealingRiverCard();

        collectBets();
        pressAnyKeyToContinue("Hand finished...\nPress enter to see who is the winner");
        return currHand.evaluateRound();

    }

    private void notifyUser(int round) {

        int currIndex;
        int p=1;

        while (true)
        {
            currIndex = (p + currHand.getDealer()) % currHand.getNumberOfPlayers();
            if (currHand.getPlayers().get(currIndex).isFolded()==false) {
                currHand.getPlayers().get(currIndex).itIsMyTurn();
                break;
            }
            p++;
        }


        System.out.println("Here is the hand state after "+round +" round(s) of bet");
        GameStateBoard.printHandState(currHand.getPlayers(),currHand.printHand());
        pressAnyKeyToContinue("Press Enter to continue the next bet round...");

    }

    private void pressAnyKeyToContinue(String str)
    {
        System.out.print(str);
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


        currHand.resetPlayersBets();
        currHand.setLastRaise(null);

        if (currHand.getRound()==0) {
            System.out.println("***First round- before dealing Flop Cards ");
            currHand.setLastRaise(currHand.getPlayers().get((2 + currHand.getDealer()) %currHand.getNumberOfPlayers()));
            System.out.println("***Last Raise was by Player with ID: " +currHand.getLastRaise().toString());
            p = 3;
           currHand.setCurrentBet(currHand.getBlinde().getBig());
        }
        else {
            System.out.println("***New bets round");
            p = 1;
            currHand.setCurrentBet(0);
        }

        while (true) {

            if (currHand.playersLeft() == 1) {
                System.out.println("***Only one player left...");
                GameStateBoard.printHandState(currHand.getPlayers(), currHand.printHand());
                break;
            }

            if (currHand.getMaxBet()==0)
            {
                System.out.println("***Max bet is 0...");
                break;
            }

            if (p>10)
            {
                System.out.println("***We stuck");
            }

            currIndex = (p + currHand.getDealer()) % currHand.getNumberOfPlayers();
            Player currPlayer =currHand.getPlayers().get(currIndex);
            currPlayer.itIsMyTurn();
            System.out.println("***Player "+currPlayer.toString() +" is playing now");


            if (currHand.getLastRaise()==currPlayer || currHand.isAllCheckOccurred()) {
                currPlayer.itIsNotMyTurn();
                System.out.println("***We finished one round of bets");
                break;
            }

            //השחקן הנוכחי הוא לא השחקן האחרון שעשה רייס וגם עוד לא פרשתי
            if (currHand.getLastRaise() != currPlayer && currPlayer.getCheckOccurred()==false) {
                //אם הוא לא פרש ויש לו כסף בקופה
                if (currPlayer.isFolded()==false
                        && currPlayer.getChips() > 0) {

                    System.out.println("***Player "+currPlayer.toString() +" is not the last player who Raised and he didn't floded and his chips is begger than 0");

                    if(currPlayer.getType()== PlayerType.Human) {
                        System.out.println("***Player is Human... I'm going to print the Hand");
                        GameStateBoard.printHandState(currHand.getPlayers(), currHand.printHand());
                    }

                  doThis(nowPlay(currPlayer),currPlayer);

                  if(currPlayer.getType()==PlayerType.Human && currPlayer.isFolded()) {
                      System.out.println("***Player is Human and he folded... we are going to end the hand");
                      currPlayer.setBet(0);
                      return;
                  }

                    System.out.println("***Pot before the action is: "+currHand.getPot());
                    currHand.addToPot(currPlayer.getBet());
                    System.out.println("***Pot after the action is: "+currHand.getPot());

                    System.out.println("***Chips player before the collection is: "+currPlayer.getChips());
                    System.out.println("***Bet player is: "+currPlayer.getBet());

                    if(currPlayer.getChips()-currPlayer.getBet()<0)
                        System.out.println("we have bug");
                    currPlayer.collectBet();
                    currPlayer.setBet(0);
                    System.out.println("***Chips player after the collection is: "+currPlayer.getChips());

                    System.out.println("***Curr max bet is: "+currHand.getMaxBet());
                    currHand.updateMaxBet();
                    System.out.println("***New max bet is: "+currHand.getMaxBet());

                } else {
                    if(currPlayer.isFolded()) {
                        System.out.println("***Player " + currPlayer.toString() + " is folded ");
                        currPlayer.setBet(0);
                    }
                    else
                    {
                        System.out.println("***Player " + currPlayer.toString() + " is  without chips");
                    }
                    //break;
                }
            }

           currPlayer.setCheckOccurred(true);
            p++;
            currPlayer.itIsNotMyTurn();

        }

    }

    private String nowPlay(Player currPlayer) {

        if (currPlayer.getType()==PlayerType.Computer) {
            System.out.println("***Player is Computer... I'm not going to print the Hand");
            return currPlayer.play();
        }
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

        System.out.println("***Here is what the player want to do: "+whatToDo);

        while (true) {

            if (whatToDo.equals("F")) {
                p.setFolded(true);
                System.out.println("***Player "+p.toString() +" is floded...");
                break;
            }

            if (whatToDo.equals("B")) {
                int betTO = 0;
                try {
                    if (currHand.getLastRaise() == null) {

                        Scanner scanner = new Scanner(System.in);
                        System.out.print("How much would you like to Bet (Number between 1 -"+currHand.getMaxBet()+")? ");
                        betTO = Integer.parseInt(scanner.nextLine());

                        if (betTO <= currHand.getCurrentBet() || betTO > p.getChips() || betTO > currHand.getMaxBet()) {
                            throw new NumberFormatException();
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
                if(currHand.getCurrentBet()>p.getBet()) {
                    currHand.subFromPot(p.getBet());
                    p.addChips(p.getBet());
                    currHand.updateMaxBet();
                    p.setBet(currHand.getCurrentBet());

                    break;
                }
                else  if(p.getType()==PlayerType.Human) {
                    System.out.println("You can't Call now ,please choose other option");
                    whatToDo=getUSerSelection();
                }
                else
                    whatToDo=p.play();

            }

            if (whatToDo.equals("R")) {
                currHand.subFromPot(p.getBet());
                p.addChips(p.getBet());
                currHand.updateMaxBet();
                int raiseTo = 0;
                while (raiseTo == 0) {
                    try {
                        if (p.getType() == PlayerType.Human) {
                            Scanner scanner = new Scanner(System.in);
                            System.out.print("What would you like to raise to (Number between 1 - "+currHand.getMaxBet()+")? ");
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

                System.out.println("***Bet before raise: "+currHand.getCurrentBet());

                currHand.setCurrentBet(currHand.getCurrentBet()+ raiseTo);
                System.out.println("***Bet after raise: "+currHand.getCurrentBet());

                currHand.setLastRaise(p);
                System.out.println("***Last Raise player is now player with ID: "+p.getId());

                for(Player player:currHand.getPlayers())
                    {
                        if(player!=p)
                            player.setCheckOccurred(false);
                    }
                    p.setBet(currHand.getCurrentBet());
                break;

            }
            if (whatToDo.equals("K")) {
                if(p.getBet()==currHand.getCurrentBet())
                {
                    if(currHand.getLastRaise()==null)
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
