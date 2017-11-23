package UI;

import Engine.GameManager;
import UI.Menus.MainMenu;

public class UIManager {
    MainMenu mainMenu=new MainMenu();
    GameManager gameManager=new GameManager();

    public void run()
    {
        mainMenu.Print();
    }
}
