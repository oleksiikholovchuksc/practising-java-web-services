package phonewebservice.parsers;


public class PhoneDOMParser extends PhoneXMLParser {

    public PhoneDOMParser(String xmlString) {
        super(xmlString);
    }

    public String getPhoneByName(String name) {
        return "Returned by DOM";
    }

}
