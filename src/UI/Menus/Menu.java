package UI.Menus;

public abstract class Menu {

   private int selectedOption;
     abstract void print ();
     public void setSelectedOption(int option)
     {
         this.selectedOption=option;
     }
}
