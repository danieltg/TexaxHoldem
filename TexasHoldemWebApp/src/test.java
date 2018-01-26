import Engine.DeckOfCards.Card;
import com.google.gson.Gson;

public class test {
    public void foo()
    {
        Card c=new Card();
        Gson gson=new Gson();
        String card=gson.toJson(c);
        System.out.println(card);
        Danieltest danieltest=new Danieltest();
    }
    public static void main (String [] args)
    {

    }
}
