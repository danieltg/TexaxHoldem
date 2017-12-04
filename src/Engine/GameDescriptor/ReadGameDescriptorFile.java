package Engine.GameDescriptor;

import Engine.Exceptions.BlindesException;
import Engine.Exceptions.GameTypeException;
import Engine.Exceptions.StructureException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import Engine.Utils.EngineUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import static Engine.GameDescriptor.Structure.validateStructure;

public class ReadGameDescriptorFile {


    public static GameDescriptor readFile(String filePath) throws IOException, ParserConfigurationException, SAXException, GameTypeException, BlindesException, StructureException {
        File fXMLFile = new File(filePath);

        if (!EngineUtils.getFileExtension(fXMLFile).equals("xml"))
        {
            throw new FileNotFoundException("Invalid file extension");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(fXMLFile);

        //doc.getDocumentElement().normalize();

        // Get the document's root XML node
        NodeList root = doc.getChildNodes();

        // Navigate down the hierarchy to get to the Engine.GameDescriptor node
        Node gameDescriptor = getNode(Tags.GAME_DESCRIPTOR, root);

        GameType type=getGameType(gameDescriptor);

        //Get to the Structure node
        assert gameDescriptor != null;
        Node structureNode = getNode(Tags.STRUCTURE, gameDescriptor.getChildNodes());

        Structure structure=getGameStructure(structureNode);
        int numberOfPlayer=0;

        switch (type){
            case Basic:
                numberOfPlayer=4;
                break;
            //TODO: we should change it
            case MultiPlayer:
            case DynamicMultiPlayer:
                numberOfPlayer=6;
                break;
        }

        if ((structure.getHandsCount()<numberOfPlayer)||(structure.getHandsCount()%numberOfPlayer)!=0)
            throw new StructureException(StructureException.INVALID_HANDSCOUNT);

        return new GameDescriptor(type,structure);
    }

    private static Structure getGameStructure(Node structureNode) throws BlindesException, StructureException {
        NodeList nodes = structureNode.getChildNodes();

        if(!EngineUtils.isNumeric(getNodeValue(Tags.HANDS_COUNT,nodes)))
            throw new StructureException(StructureException.HANDSCOUNT_NOT_A_NUMBER);

        if (!EngineUtils.isNumeric(getNodeValue(Tags.BUY, nodes)))
            throw new StructureException(StructureException.BUY_NOT_A_NUMBER);

        int handsCount=Integer.parseInt(getNodeValue(Tags.HANDS_COUNT,nodes));
        int buy = Integer.parseInt(getNodeValue(Tags.BUY, nodes));

        Node blindesNode = getNode(Tags.BLINDES, structureNode.getChildNodes());
        Blindes blinde=getBlindes(blindesNode);
        Structure structure=new Structure(handsCount,buy,blinde);
        validateStructure(structure);
        return structure;
    }

    private static Blindes getBlindes(Node blindes) throws BlindesException {
        int additions=0;
        int maxTotalRounds=0;

        NodeList nodes = blindes.getChildNodes();

        if(!EngineUtils.isNumeric(getNodeValue(Tags.BIG,nodes)))
            throw new BlindesException(BlindesException.BIG_NOT_A_NUMBER);

        if(!EngineUtils.isNumeric(getNodeValue(Tags.SMALL,nodes)))
            throw new BlindesException(BlindesException.SMALL_NOT_A_NUMBER);

        boolean fixed=Boolean.parseBoolean(getNodeAttr(Tags.BLINDES_FIXED, blindes));
        if (!fixed)
        {
            if(!EngineUtils.isNumeric(getNodeAttr(Tags.BLINDES_ADDITIONS, blindes)))
                throw new BlindesException(BlindesException.ADDITIONS_NOT_A_NUMBER);

            if(!EngineUtils.isNumeric(getNodeAttr(Tags.BLINDES_MAX_TOTAL_ROUNDS, blindes)))
                throw new BlindesException(BlindesException.NEGATIVE_MAX_TOTAL_NOT_A_NUMBER);

            additions=Integer.parseInt(getNodeAttr(Tags.BLINDES_ADDITIONS, blindes));
            maxTotalRounds=Integer.parseInt(getNodeAttr(Tags.BLINDES_MAX_TOTAL_ROUNDS, blindes));
        }

        int big=Integer.parseInt(getNodeValue(Tags.BIG,nodes));
        int small=Integer.parseInt(getNodeValue(Tags.SMALL,nodes));

        return new Blindes(big,small,fixed,additions,maxTotalRounds);
    }

    private static GameType getGameType(Node gameDescriptor) throws GameTypeException {
        NodeList nodes= gameDescriptor.getChildNodes();
        try
        {
            return GameType.valueOf(getNodeValue(Tags.GAME_TYPE,nodes));
        }
        catch (IllegalArgumentException e ) {
            throw new GameTypeException(GameTypeException.INVALID_VALUE);
        }
    }

    private static Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }

    private static String getNodeValue( Node node ) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++ ) {
            Node data = childNodes.item(x);
            if ( data.getNodeType() == Node.TEXT_NODE )
                return data.getNodeValue();
        }
        return "";
    }

    private static String getNodeValue(String tagName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.TEXT_NODE )
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }

    private static String getNodeAttr(String attrName, Node node ) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++ ) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }

    private static String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                        if ( data.getNodeName().equalsIgnoreCase(attrName) )
                            return data.getNodeValue();
                    }
                }
            }
        }

        return "";
    }


}
