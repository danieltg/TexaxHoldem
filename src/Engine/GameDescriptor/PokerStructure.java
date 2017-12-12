package Engine.GameDescriptor;


//<HandsCount>10</HandsCount>
//<Buy>100</Buy>
//<Blindes fixed="true">
//  <Big>4</Big>
//	<Small>2</Small>
//</Blindes>

import Engine.Exceptions.BlindesException;
import Engine.Exceptions.StructureException;
import Jaxb.Structure;

import java.io.Serializable;

import static Engine.GameDescriptor.PokerBlindes.validateBlindes;

public class PokerStructure implements Serializable {

    private int handsCount;
    private int buy;
    private PokerBlindes blindes;


    public PokerStructure (Structure s)
    {
        this.handsCount=s.getHandsCount().intValue();
        this.buy=s.getBuy().intValue();
        this.blindes= new PokerBlindes(s.getBlindes());
    }

    public PokerStructure(int handsCount, int buy, PokerBlindes blindes) {
        this.handsCount = handsCount;
        this.buy = buy;
        this.blindes = blindes;
    }

    //Getter
    public int getHandsCount() {
        return handsCount;
    }

    public int getBuy() {
        return buy;
    }

    public PokerBlindes getBlindes() {
        return blindes;
    }

    //Setter
    public void setHandsCount(int handsCount) {
        this.handsCount = handsCount;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public void setBlindes(PokerBlindes blindes) {
        this.blindes = blindes;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "handsCount=" + handsCount +
                ", buy=" + buy +
                ", blindes=" + blindes +
                '}';
    }

    public static void validateStructure(PokerStructure s) throws BlindesException, StructureException {
        if (s.getHandsCount()<0 )
            throw new StructureException(StructureException.NEGATIVE_HANDSCOUNT);

        if (s.getBuy()<0)
            throw new StructureException(StructureException.NEGATIVE_BUY);

        if (!validateBlindes(s.getBlindes()))
            throw new BlindesException(BlindesException.ILLEGAL_BLINDES);

    }
}
