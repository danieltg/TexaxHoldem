package UI;

import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GameManager;
import UI.Menus.MainMenu;

import java.util.Scanner;

public class UIManager {
    private final static int MAX_OPTION = 8;
    private final static int MIN_OPTION = 1;

    private boolean gameEnded;

    MainMenu mainMenu;
    GameManager gameManager;


    public UIManager() {
        this.gameEnded = false;
        this.mainMenu = new MainMenu();
    }

    private void ReadSettingsXML() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a file name:");
            System.out.flush();
            String filename = scanner.nextLine();

            GameDescriptor gameDescriptor = ReadGameDescriptorFile.readFile(filename);
            gameManager=new GameManager(gameDescriptor);
            System.out.println(gameDescriptor.toString());

        } catch (Exception e) {
            System.out.println("Invalid Game Descriptor file: " + e.getMessage() + ".");
        }
    }

    public void run() {
        int selectedMainOption = mainMenu.Print();

        while (!gameEnded) {

            if (isValidOption(selectedMainOption)) {
                runOption(selectedMainOption);
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

    private boolean isValidOption(int selectedMainOption) {
        if(selectedMainOption>=MIN_OPTION&&selectedMainOption<MAX_OPTION)
            return true;
        else return false;

    }
}
