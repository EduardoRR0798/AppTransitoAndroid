/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.sms;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Frost
 */
public class SmsHandler extends DefaultHandler {
    private String response = "";
    private StringBuilder builder = new StringBuilder();
    private String _return = "";
    
    public SmsHandler() {
    }
    
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getReturn() {
        return _return;
    }

    public void setReturn(String _return) {
        this._return = _return;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        if (response != null && builder != null) {            
            String val = builder.toString().trim();
            if (!val.isEmpty()) {
                try {
                    if(name.compareToIgnoreCase("return")==0){
                        _return = val;                    }
                } catch (Exception e) { e.printStackTrace(); }
                builder.setLength(0);
            }
        }
    }
    /** Ejemplo respuesta Web Service ***
     <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/">
        <SOAP-ENV:Body>
           <ns1:EnviaMensajeOLResponse xmlns:ns1="http://www.calixtaondemand.com/wsdl">
              <return xsi:type="xsd:int">3</return>
           </ns1:EnviaMensajeOLResponse>
        </SOAP-ENV:Body>
     </SOAP-ENV:Envelope>
     
     {return = "3"}
     */
    
}