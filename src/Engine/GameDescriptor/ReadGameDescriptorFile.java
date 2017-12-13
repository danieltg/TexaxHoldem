package Engine.GameDescriptor;

import Engine.Exceptions.BlindesException;
import Engine.Exceptions.StructureException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import Engine.Utils.EngineUtils;
import Jaxb.GameDescriptor;

import java.io.File;
import java.io.FileNotFoundException;

public class ReadGameDescriptorFile {


    public static PokerGameDescriptor readFile(String filePath) throws FileNotFoundException, JAXBException, StructureException, BlindesException {

        File file = new File(filePath);
        if (!EngineUtils.getFileExtension(file).equals("xml"))
            throw new FileNotFoundException("Invalid file extension");

        JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        GameDescriptor gameDescriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);

        PokerGameDescriptor pokerGameDescriptor = new PokerGameDescriptor(gameDescriptor);
        validatePokerGameDescriptor(pokerGameDescriptor);

        return pokerGameDescriptor;

    }


    private static void validatePokerGameDescriptor(PokerGameDescriptor game) throws StructureException, BlindesException {

        PokerStructure s=game.getStructure();
        PokerBlindes b=s.getBlindes();

        switch (game.getType()) {
            case Basic: {
                if ((s.getHandsCount() < 4) || (s.getHandsCount() % 4) != 0)
                    throw new StructureException(StructureException.INVALID_HANDSCOUNT);
                break;
            }
            case MultiPlayer:
            case DynamicMultiPlayer:
            {
                if ((s.getHandsCount() < 3) || (s.getHandsCount() >6))
                    throw new StructureException(StructureException.INVALID_HANDSCOUNT_EX2);
                break;
            }
        }

        if (s.getHandsCount()<0 )
            throw new StructureException(StructureException.NEGATIVE_HANDSCOUNT);

        if (s.getBuy()<0)
            throw new StructureException(StructureException.NEGATIVE_BUY);

        if (b.getSmall()<0 )
            throw new BlindesException(BlindesException.NEGATIVE_SMALL);

        if (b.getBig()<0)
            throw new BlindesException(BlindesException.NEGATIVE_BIG);

        if(b.getAdditions()<0)
            throw new BlindesException(BlindesException.NEGATIVE_ADDITIONS);

        if(b.getMaxTotalRounds()<0)
            throw new BlindesException(BlindesException.NEGATIVE_MAX_TOTAL_ROUNDS);

        if(b.getSmall()>=b.getBig())
            throw new BlindesException(BlindesException.SMALL_BIGGER_THEN_SMALL);

    }

}
