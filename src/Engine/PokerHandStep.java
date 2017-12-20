package Engine;

import Engine.Players.PokerPlayer;

import java.io.Serializable;
import java.util.List;

public class PokerHandStep implements Serializable {

    private List<PokerPlayer> players;
    private int activePlayer;
    private int pot;
    private String tableCards;
    private int currentBet;   //Current bet amount that must be called/raised
    private String action;
    private int additionActionInformation;

    public PokerHandStep(List<PokerPlayer> players, int activePlayer, int pot, String tableCards, int currentBet, String action, int additionActionInformation) {
        this.players = players;
        this.activePlayer = activePlayer;
        this.pot = pot;
        this.tableCards = tableCards;
        this.currentBet = currentBet;
        this.action = action;
        this.additionActionInformation = additionActionInformation;
    }


    private int getplayerIndexById()
    {
        for (int i=0; i<players.size(); i++)
        {
            if (players.get(i).getId()==activePlayer)
                return i;
        }

        return -999;
    }

    public String getStepAsString()
    {
        String message;

        if (activePlayer==-999) {
            message = "The step is not related to any player\n";
            if (action.equals("PLAYER_CARDS")) {
                message = message + "Players cards:\n";
                for (PokerPlayer p: players)
                    message=message+"Player <"+p.getName()+", "+p.getId()+"> : "+p.getHoleCards()+"\n";
            }
            else
                message=message+"Cards: "+tableCards+"\n";
        }
        else {
            int indexInList=getplayerIndexById();
            message = "Active player ID: " + activePlayer + "\n";
            message=message+"Player name: "+players.get(indexInList).getName()+"\n";
            message=message+"Player type: "+players.get(indexInList).getType()+"\n";
            message=message+"Player cards: "+players.get(indexInList).getHoleCards()+"\n";
            message=message+"Player chips: "+players.get(indexInList).getChips()+"\n";
            message=message+"Player bet: "+players.get(indexInList).getBet()+"\n";
        }

        message=message+"Pot: "+pot+"\n";
        message=message+"Current Bet: "+currentBet+"\n";
        message=message+"Action: "+action+"\n";

        if (action=="R" || action=="B")
            message=message+"Additional Action Information: "+additionActionInformation+"\n";


        return message;
    }

}
