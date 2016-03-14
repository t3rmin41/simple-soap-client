package my.simple.soap.client;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSoapClient {

    private static final Logger log = LoggerFactory.getLogger(SimpleSoapClient.class);
    
    public static void main(String args[]) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://localhost:9999/ws/countries.wsdl";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

        // print SOAP Response
        log.info("Response SOAP Message:");
        soapResponse.writeTo(System.out);

        soapConnection.close();
    }

    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://my.soap.webservice.io/countries/countries-web-service";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("coun", serverURI);

        //Constructed SOAP Request Message:
        /*
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:coun="http://my.soap.webservice.io/countries/countries-web-service">
        <soapenv:Header/>
            <soapenv:Body>
                <coun:getCountryRequest>
                    <coun:name>Spain</coun:name>
                </coun:getCountryRequest>
            </soapenv:Body>
        </soapenv:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getCountryRequest", "coun");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("name", "coun");
        soapBodyElem1.addTextNode("Spain");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "getCountryRequest");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

}
