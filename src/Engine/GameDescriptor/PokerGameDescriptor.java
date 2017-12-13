package Engine.GameDescriptor;

import Jaxb.GameDescriptor;

import java.io.Serializable;

public class PokerGameDescriptor implements Serializable {

    private GameType type;
    private PokerStructure structure;

    public final static int NUM_OF_HUMAN_PLAYERS= 1;
    public final static int NUM_OF_COMPUTER_PLAYERS= 3;

    public PokerGameDescriptor (GameDescriptor g)
    {
        type= GameType.valueOf(g.getGameType());
        structure=new PokerStructure((g.getStructure()));
    }

    public GameType getType() {
        return type;
    }

    public PokerStructure getStructure() {
        return structure;
    }

    @Override
    public String toString() {
        return "GameDescriptor:\n" +
                "\tType=" + type +
                "\n\tStructure:\n"+
                "\t\tHandsCount="+structure.getHandsCount()+
                "\n\t\tBuy="+structure.getBuy()+
                "\n\t\tBlindes:\n"+
                "\t\t\tFixed="+structure.getBlindes().isFixed()+
                "\n\t\t\tBig="+structure.getBlindes().getBig()+
                "\n\t\t\tSmall="+structure.getBlindes().getSmall()+
                "\n\t\t\tAdditions="+structure.getBlindes().getAdditions()+
                "\n\t\t\tMax total rounds="+structure.getBlindes().getMaxTotalRounds();

    }
}
