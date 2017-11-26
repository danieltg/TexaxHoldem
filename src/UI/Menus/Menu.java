package UI.Menus;

public abstract class Menu {

   protected int selectedOption;

   abstract void Print ();
    public int getSelectedOption() {
        return selectedOption;
    }
}
