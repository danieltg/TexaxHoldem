package UI;

import Engine.GameManager;
import UI.Menus.MainMenu;

public class UIManager {
    final int MAX_OPTION=8;
    final int MIN_OPTION=1;
    MainMenu mainMenu=new MainMenu();
    GameManager gameManager=new GameManager();
    int selectedMainOption;

    public void run()
    {
       selectedMainOption= mainMenu.Print();
       while(!isValidOption(selectedMainOption))
       {
           System.out.println("This number is not exist on menu ,try again!");
           selectedMainOption= mainMenu.Print();
       }
       runOption(selectedMainOption);
        
    }

    private void runOption(int selectedMainOption) {
        switch (selectedMainOption) {
            case 1:
            case 2:
            case 3:



        }
    }

    private boolean isValidOption(int selectedMainOption) {
        if(selectedMainOption>=MIN_OPTION&&selectedMainOption<MAX_OPTION)
            return true;
        else return false;

    }
}
