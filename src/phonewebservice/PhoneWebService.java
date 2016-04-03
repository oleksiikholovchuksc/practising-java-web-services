package phonewebservice;

import org.xml.sax.SAXException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.IOException;


@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PhoneWebService {
    @WebMethod
    public String getPhoneByNameUsingSAX(String name)
            throws SAXException,
                   IOException;

    @WebMethod
    public String getPhoneByNameUsingDOM(String name);
}