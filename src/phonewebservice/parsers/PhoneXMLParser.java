package phonewebservice.parsers;

import org.xml.sax.SAXException;

import javax.naming.ldap.UnsolicitedNotification;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public abstract class PhoneXMLParser {

    private String xmlString;

    public PhoneXMLParser(String xmlStringToParse) {
        xmlString = xmlStringToParse.replaceAll("\\s", "");
    }

    public abstract String getPhoneByName(String name)
            throws SAXException,
                   IOException;

    public final String getXMLString() {
        return xmlString;
    }

    protected ByteArrayInputStream getInputStream()
                    throws UnsupportedEncodingException {
        return new ByteArrayInputStream(xmlString.getBytes("utf-8"));
    }

}
