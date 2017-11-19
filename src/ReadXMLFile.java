
import Exceptions.BlindesException;
import Exceptions.GameTypeException;
import Exceptions.StructureException;
import GameDescriptor.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import GameDescriptor.Structure;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.File;
import org.w3c.dom.NamedNodeMap;

import static GameDescriptor.Structure.validateStructure;

public class ReadXMLFile {


    public static void main(String argv[]) {

        //Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter a file name: ");
        //System.out.flush();
        //String filename = scanner.nextLine();
        readFile("/Users/danielt/Desktop/Other/ex1/ex1-basic.xml");
        //File file = new File(filename);


    }

    public static void readFile(String filePath)
    {
        try{
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            // Get the document's root XML node
            NodeList root = doc.getChildNodes();

            // Navigate down the hierarchy to get to the GameDescriptor node
            Node gameDescriptor = getNode(Tags.GAME_DESCRIPTOR, root);

            try{

                GameType type=getGameType(gameDescriptor);

                //Get to the Structure node
                Node structureNode = getNode(Tags.STRUCTURE, gameDescriptor.getChildNodes());

                Structure structure=getGameStructure(structureNode);
                GameDescriptor descriptor=new GameDescriptor(type,structure);
                System.out.println(descriptor.toString());
            }
            catch (Exception e)
            {
                System.out.println("Invalid Game Descriptor file: "+e.getMessage()+".");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Structure getGameStructure(Node structureNode) throws BlindesException, StructureException {
        NodeList nodes = structureNode.getChildNodes();

        if(!isNumeric(getNodeValue(Tags.HANDS_COUNT,nodes)))
            throw new StructureException(StructureException.HANDSCOUNT_NOT_A_NUMBER);

        if (!isNumeric(getNodeValue(Tags.BUY, nodes)))
            throw new StructureException(StructureException.BUY_NOT_A_NUMBER);

        int handsCount=Integer.parseInt(getNodeValue(Tags.HANDS_COUNT,nodes));
        int buy = Integer.parseInt(getNodeValue(Tags.BUY, nodes));

        Node blindesNode = getNode(Tags.BLINDES, structureNode.getChildNodes());
        Blindes blinde=getBlindes(blindesNode);
        Structure structure=new Structure(handsCount,buy,blinde);
        validateStructure(structure);
        return structure;
    }

    public static Blindes getBlindes(Node blindes) throws BlindesException {
        int additions=0;
        int maxTotalRounds=0;

        NodeList nodes = blindes.getChildNodes();

        if(!isNumeric(getNodeValue(Tags.BIG,nodes)))
            throw new BlindesException(BlindesException.BIG_NOT_A_NUMBER);

        if(!isNumeric(getNodeValue(Tags.SMALL,nodes)))
            throw new BlindesException(BlindesException.SMALL_NOT_A_NUMBER);

        boolean fixed=Boolean.parseBoolean(getNodeAttr(Tags.BLINDES_FIXED, blindes));
        if (!fixed)
        {
            if(!isNumeric(getNodeAttr(Tags.BLINDES_ADDITIONS, blindes)))
                throw new BlindesException(BlindesException.ADDITIONS_NOT_A_NUMBER);

            if(!isNumeric(getNodeAttr(Tags.BLINDES_MAX_TOTAL_ROUNDS, blindes)))
                throw new BlindesException(BlindesException.NEGATIVE_MAX_TOTAL_NOT_A_NUMBER);

            additions=Integer.parseInt(getNodeAttr(Tags.BLINDES_ADDITIONS, blindes));
            maxTotalRounds=Integer.parseInt(getNodeAttr(Tags.BLINDES_MAX_TOTAL_ROUNDS, blindes));
        }

        int big=Integer.parseInt(getNodeValue(Tags.BIG,nodes));
        int small=Integer.parseInt(getNodeValue(Tags.SMALL,nodes));

        return new Blindes(big,small,fixed,additions,maxTotalRounds);
    }

    public static GameType getGameType(Node gameDescriptor) throws GameTypeException {
        NodeList nodes= gameDescriptor.getChildNodes();
        try
        {
            return GameType.valueOf(getNodeValue(Tags.GAME_TYPE,nodes));
        }
        catch (IllegalArgumentException e ) {
            throw new GameTypeException(GameTypeException.INVALID_VALUE);
        }
    }

    public static Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }

    public static String getNodeValue( Node node ) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++ ) {
            Node data = childNodes.item(x);
            if ( data.getNodeType() == Node.TEXT_NODE )
                return data.getNodeValue();
        }
        return "";
    }

    public static String getNodeValue(String tagName, NodeList nodes ) {
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

    public static String getNodeAttr(String attrName, Node node ) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++ ) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }

    public static String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
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

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}
