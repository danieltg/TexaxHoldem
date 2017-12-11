package UI.Menus;

import java.util.Scanner;

public class HandMenu {

    public static void print() {
        System.out.println("================================================");
        System.out.println("|   Hand MENU                                  |");
        System.out.println("================================================");
        System.out.println("| Please make a selection:                     |");
        System.out.println("|   F for Fold                                 |");
        System.out.println("|   B for Bet                                  |");
        System.out.println("|   C for Call                                 |");
        System.out.println("|   K for Check                                |");
        System.out.println("|   R for Raise                                |");
        System.out.println("================================================");
    }

    public static String getOptionFromUser() {

        Scanner scanner=new Scanner(System.in);
        System.out.print("Please enter your choice: ");
        System.out.flush();
        String choice = scanner.nextLine();
        if (isValidOption(choice))
            return choice.toUpperCase();
        else
            throw new NumberFormatException("Invalid input. Its have to be: F | B | C | K | R ");
    }

    private static boolean isValidOption(String selectedMainOption) {
        return selectedMainOption != null &&
                (selectedMainOption.toUpperCase().equals("F") || selectedMainOption.toUpperCase().equals("B") ||
                        selectedMainOption.toUpperCase().equals("C") || selectedMainOption.toUpperCase().equals("K") ||
                        selectedMainOption.toUpperCase().equals("R"));

    }
}
