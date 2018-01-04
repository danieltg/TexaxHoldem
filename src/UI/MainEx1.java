package UI;


import Engine.Players.PokerPlayer;
import com.rundef.poker.EquityCalculator;
import com.rundef.poker.Hand;

public class MainEx1 {

    public static void main(String[] args) throws Exception {


//        EquityCalculator calculator = new EquityCalculator();
//        calculator.reset();
//
//        calculator.addHand(Hand.fromString("9C3C"));
//        calculator.addHand(Hand.fromString("QSKC"));
//        calculator.addHand(Hand.fromString("JSQH"));
//
//        calculator.calculate();
//
//        System.out.println("Hand 1 Equity: "+calculator.getHandEquity(0));
//        System.out.println("Hand 2 Equity: "+calculator.getHandEquity(1));
//        System.out.println("Hand 3 Equity: "+calculator.getHandEquity(2));


        //Test
        UIManager uiManager = new UIManager();
        uiManager.run();
    }
}
