package UI.Menus;

import javax.swing.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Scanner;

public class MainMenu extends Menu {

    private final static int MAX_OPTION = 8;
    private final static int MIN_OPTION = 1;

    @Override
    public void Print() {

        System.out.println("=======================================");
        System.out.println("|   Texas hold'em MENU                |");
        System.out.println("=======================================");
        System.out.println("| Please Make a selection:            |");
        System.out.println("|   1. Load game configuration file   |");
        System.out.println("|   2. Start game                     |");
        System.out.println("|   3. Show State game                |");
        System.out.println("|   4. Run one hand                   |");
        System.out.println("|   5. Get statistics                 |");
        System.out.println("|   6. Leave the Game                 |");
        System.out.println("|   7. Save game to file              |");
        System.out.println("|   8. Load game from file            |");
        System.out.println("=======================================");
    }

    public int GetOptionFromUser() {

        int choise=0;
        System.out.print("Please enter your choice: ");
        Scanner scanner=new Scanner(System.in);
        choise = Integer.parseInt(scanner.next());
        if (isValidOption(choise))
            return choise;
        else
            throw new NumberFormatException();
    }

    private boolean isValidOption(int selectedMainOption) {
        return (selectedMainOption>=MIN_OPTION&&selectedMainOption<MAX_OPTION);
    }
}
