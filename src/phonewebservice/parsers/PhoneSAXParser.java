package phonewebservice.parsers;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import java.io.IOException;


public class PhoneSAXParser extends PhoneXMLParser {

    private SAXParser parser;
    private PhoneSAXParserImpl xmlNodeHandler;

    public PhoneSAXParser(String xmlString)
            throws ParserConfigurationException,
                   SAXException
    {
        super(xmlString);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
        xmlNodeHandler = new PhoneSAXParserImpl();
    }

    public String getPhoneByName(String name)
            throws SAXException,
                   IOException
    {
        xmlNodeHandler.setNameToSearch(name);

        String resultPhone = "";
        try {
            parser.parse(getInputStream(), xmlNodeHandler);
        } catch(PhoneSAXParserImpl.NameFoundException ex) {
            if(ex.getName().equals(name)) {
                resultPhone = ex.getPhone();
            }
        } catch(PhoneSAXParserImpl.NameNotFoundException ex) {
            resultPhone = "";
        }

        return resultPhone;
    }

    private class PhoneSAXParserImpl extends DefaultHandler {

        private final String HUMAN_DATA_LABEL  = "human_data";
        private final String NAME_LABEL        = "name";
        private final String PHONE_LABEL       = "phone";

        private boolean nameFound = false;
        private boolean phoneFound = false;

        private StringBuilder nameStringBuilder = new StringBuilder();
        private StringBuilder phoneStringBuilder = new StringBuilder();
        private StringBuilder currentStringBuilder = null;

        private String nameToSearch;

        public class NameFoundException extends SAXException {
            private String name;
            private String phone;

            public NameFoundException(String nameFound, String phoneFound) {
                name = nameFound;
                phone = phoneFound;
            }

            public String getName() {
                return name;
            }

            public String getPhone() {
                return phone;
            }
        }

        public class NameNotFoundException extends SAXException {}

        public void setNameToSearch(String name) {
            nameToSearch = name;
        }

        @Override
        public void endDocument() throws NameNotFoundException {
            throw new NameNotFoundException();
        }

        @Override
        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes atts)
            throws SAXException
        {
            if(qName.equals(NAME_LABEL)) {
                currentStringBuilder = nameStringBuilder;
            }
            else if(qName.equals(PHONE_LABEL)) {
                currentStringBuilder = phoneStringBuilder;
            }
        }

        @Override
        public void endElement(String namespaceURI,
                               String localName,
                               String qName)
            throws SAXException
        {
            currentStringBuilder = null;

            if(qName.equals(PHONE_LABEL)) {
                phoneFound = true;
            }
            else if(qName.equals(NAME_LABEL)) {
                nameFound = true;
            }
            else if(qName.equals(HUMAN_DATA_LABEL)) {
                boolean nameWasFound = nameFound;
                boolean phoneWasFound = phoneFound;

                nameFound = false;
                phoneFound = false;

                String resultName = nameStringBuilder.toString();
                String resultPhone = phoneStringBuilder.toString();

                nameStringBuilder.setLength(0);
                phoneStringBuilder.setLength(0);

                if(nameWasFound && phoneWasFound) {
                    if(resultName.equals(nameToSearch)) {
                        throw new NameFoundException(resultName, resultPhone);
                    }
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if(currentStringBuilder != null) {
                currentStringBuilder.append(new String(ch, start, length));
            }
        }

    }

}
