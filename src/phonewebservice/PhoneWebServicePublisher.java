package phonewebservice;

import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.ws.Endpoint;


public class PhoneWebServicePublisher {
    public static void main(String[] args)
            throws IOException,
                   SAXException,
                   ParserConfigurationException
    {
        Endpoint.publish("http://localhost:1986/phonewebservice", new PhoneWebServiceImpl());
    }
}