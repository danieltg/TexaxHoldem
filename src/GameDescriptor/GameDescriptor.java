package GameDescriptor;

public class GameDescriptor {

    private GameType type;
    private Structure structure;


    public GameDescriptor(GameType type, Structure structure) {
        this.type = type;
        this.structure = structure;
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
