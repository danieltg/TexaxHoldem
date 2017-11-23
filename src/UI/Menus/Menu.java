package UI.Menus;

public abstract class Menu {

   protected int selectedOption;

   abstract int Print ();


    public int getSelectedOption() {
        return selectedOption;
    }
}
