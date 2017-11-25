package UI;

import Engine.CurrGameState;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;
import Engine.Players.ComputerPlayer;
import Engine.Players.HumanPlayer;
import Engine.Players.Player;
import UI.Boards.GameStateBoard;
import UI.Menus.MainMenu;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UIManager {
    private final static int MAX_OPTION = 8;
    private final static int MIN_OPTION = 1;

    private boolean gameEnded;

    MainMenu mainMenu;
    GameManager gameManager=new GameManager();
    GameStateBoard board = new GameStateBoard();

    public UIManager() {
        this.gameEnded = false;
        this.mainMenu = new MainMenu();
    }

    private void ReadSettingsXML() {

        if (gameManager.GetStateOfGame() == CurrGameState.NotInitialized||gameManager.GetStateOfGame()==CurrGameState.Ended) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a file name:");
                System.out.flush();
                String filename = scanner.nextLine();
                gameManager.setGameDescriptor(ReadGameDescriptorFile.readFile(filename));

                //just for check- need to delete
               System.out.println(gameManager.GetGameDescriptor().toString());

                //TODO:need to delete the check Playersarray and send the real plaeyrs array from engine
                List<Player> players = new ArrayList<Player>();
                players.add(new HumanPlayer());
                players.get(0).setChips(100);
                players.add(new ComputerPlayer());
                players.get(1).setChips(600);
                players.add(new HumanPlayer());
                players.get(2).setChips(400);
                players.add(new ComputerPlayer());
                players.get(3).setChips(200);
                board.print(players);


            } catch (Exception e) {
                System.out.println("Invalid Game Descriptor file: " + e.getMessage() + ".");
            }
        }
        else{
            System.out.println("cant LoadXml ");
        }
    }


    public void run() {
        int selectedMainOption = mainMenu.Print();

        while (!gameEnded) {

            if (isValidOption(selectedMainOption)) {
                runOption(selectedMainOption);
                selectedMainOption = mainMenu.Print();
            } else {
                System.out.println("This number is not exist on menu ,try again!");
                selectedMainOption = mainMenu.Print();
            }
        }
    }
    private void runOption(int selectedMainOption) {
        switch (selectedMainOption) {
            case 1: ReadSettingsXML();
            case 2: StartGame();
            case 3: ShowGameState();
            case 4: RunOneHand();
            case 5: GetStatistics();
            case 6: LeaveTheGame();
            case 7: SaveGame();
            case 8: LoadGame();



        }
    }

    private void LoadGame() {
    }


    private void SaveGame() {
    }

    private void LeaveTheGame() {
    }

    private void GetStatistics() {
    }

    private void RunOneHand() {
    }

    private void ShowGameState() {
    }

    private void StartGame() {
    }


    //TODo: add excptions.
    private boolean isValidOption(int selectedMainOption) {
        if(selectedMainOption>=MIN_OPTION&&selectedMainOption<MAX_OPTION)
            return true;
        else return false;

    }
}
