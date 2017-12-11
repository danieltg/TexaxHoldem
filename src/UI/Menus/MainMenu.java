package UI.Menus;

import java.util.Scanner;

public class MainMenu extends Menu {

    private final static int MAX_OPTION = 10;
    private final static int MIN_OPTION = 1;

    @Override
    public void Print() {

        System.out.println("================================================");
        System.out.println("|   Texas Hold'em MENU                         |");
        System.out.println("================================================");
        System.out.println("| Please select one of the following options:  |");
        System.out.println("|   1.  Load game configuration file           |");
        System.out.println("|   2.  Start game                             |");
        System.out.println("|   3.  Show game state                        |");
        System.out.println("|   4.  Run one hand                           |");
        System.out.println("|   5.  Get statistics                         |");
        System.out.println("|   6.  Buy $$$                                |");
        System.out.println("|   7.  Quit game                              |");
        System.out.println("|   8.  Save game to file                      |");
        System.out.println("|   9.  Load game from file                    |");
        System.out.println("|   10. Quit program                           |");
        System.out.println("================================================");
    }

    public int getOptionFromUser() {

        Scanner scanner=new Scanner(System.in);
        System.out.print("Please enter your choice: ");
        System.out.flush();
        int choice = Integer.parseInt(scanner.nextLine());
        if (isValidOption(choice))
            return choice;
        else
            throw new NumberFormatException();
    }

    private boolean isValidOption(int selectedMainOption) {
        return (selectedMainOption>=MIN_OPTION&&selectedMainOption<=MAX_OPTION);
    }
}
