package phonewebservice;

import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Files;

import org.xml.sax.SAXException;
import phonewebservice.parsers.*;


@WebService(endpointInterface = "phonewebservice.PhoneWebService")
public class PhoneWebServiceImpl implements PhoneWebService {

    private final String DATABASE_FILE_NAME =
            "/Users/vortexxx192/IdeaProjects/WebService/src/phonewebservice/database.xml";

    private PhoneSAXParser phoneSAXParser;
    private PhoneDOMParser phoneDOMParser;

    PhoneWebServiceImpl()
            throws IOException,
                   SAXException,
                   ParserConfigurationException
    {
        String xmlString = new String(
                Files.readAllBytes(Paths.get(DATABASE_FILE_NAME)));

        phoneSAXParser = new PhoneSAXParser(xmlString);
        phoneDOMParser = new PhoneDOMParser(xmlString);
    }

    @Override
    public String getPhoneByNameUsingSAX(String name)
            throws SAXException,
                   IOException
    {
        return phoneSAXParser.getPhoneByName(name);
    }

    @Override
    public String getPhoneByNameUsingDOM(String name) {
        return phoneDOMParser.getPhoneByName(name);
    }

}