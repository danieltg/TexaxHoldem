package UI.Menues;

public class MainMenu extends Menu {
    @Override
    public void print() {
        System.out.println("Hi, Please enter one option:");
        System.out.println("1. Load game configurtaion file.");
        System.out.println("2. Start game.");
        System.out.println("3. Show State game.");
        System.out.println("4. Run one hand.");
        System.out.println("5. Get statistics.");
        System.out.println("6. Leave the Game.");
        System.out.println("7. Save game to file.");
        System.out.println("8. Load game from file.");

    }
}
