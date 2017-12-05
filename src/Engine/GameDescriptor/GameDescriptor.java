package Engine.GameDescriptor;

import java.io.Serializable;

public class GameDescriptor implements Serializable {

    private GameType type;
    private Structure structure;

    public final static int NUM_OF_HUMAN_PLAYERS= 1;
    public final static int NUM_OF_COMPUTER_PLAYERS= 3;

    public GameDescriptor(GameType type, Structure structure) {
        this.type = type;
        this.structure = structure;
    }

    public GameType getType() {
        return type;
    }

    public Structure getStructure() {
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
