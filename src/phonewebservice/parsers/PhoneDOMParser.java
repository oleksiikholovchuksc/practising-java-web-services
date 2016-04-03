package phonewebservice.parsers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;


public class PhoneDOMParser extends PhoneXMLParser {

    private PhoneDOMParserImpl parserImpl;

    public PhoneDOMParser(String xmlString) throws ParserConfigurationException,
                                                   IOException,
                                                   SAXException
    {
        super(xmlString);

        parserImpl = new PhoneDOMParserImpl();
    }

    public String getPhoneByName(String name) {
        return parserImpl.getPhoneByName(name);
    }

    private class PhoneDOMParserImpl {

        private final String NAME_LABEL   = "name";
        private final String PHONE_LABEL  = "phone";

        Document document;

        PhoneDOMParserImpl() throws ParserConfigurationException,
                                    IOException,
                                    SAXException
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(getInputStream());
        }

        public String getPhoneByName(String name) {
            return recursiveSearch(document.getDocumentElement(), name);
        }

        private String recursiveSearch(Node node, String nameToSearch) {
            if(!node.hasChildNodes()) {
                return "";
            }

            // search for needed labels among direct children
            String name = "", phone = "";
            Node childNode = node.getFirstChild();
            while(childNode != null) {
                String textContent = childNode.getTextContent();
                if(childNode.getNodeName().equals(NAME_LABEL)) {
                    name = textContent;
                }
                else if(childNode.getNodeName().equals(PHONE_LABEL)) {
                    phone = textContent;
                }

                childNode = childNode.getNextSibling();
            }

            if(name.equals(nameToSearch) && !phone.isEmpty()) {
                return phone;
            }

            // if nothing found, then continue recursive search
            childNode = node.getFirstChild();
            while(childNode != null) {
                String childSearchResult = recursiveSearch(childNode, nameToSearch);
                if(!childSearchResult.isEmpty()) {
                    return childSearchResult;
                }

                childNode = childNode.getNextSibling();
            }

            return "";
        }

    }

}
