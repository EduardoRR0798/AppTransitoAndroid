/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.sms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Frost
 */
public class JaxSms {
    /** JAX = Java API for XML */
    private final String URL_END_POINT;
    private final String ID_CLIENTE;
    private final String EMAIL;
    private final String PASSWORD;
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy/hh/mm");
    
    public JaxSms(){
        URL_END_POINT = "https://www.calixtaondemand.com/ws/webServiceV8.php";
        ID_CLIENTE = "46657";
        EMAIL = "alberto.hernandez@hightek.com.mx";
        PASSWORD = "5aa381482c45ecd2abdae944012c6126eb2f3ddf6f061c8176e00629277242c6";
    }
    
    public String enviar(String telefono, String mensaje) {
        Calendar fecha = Calendar.getInstance();
        fecha.add(Calendar.MINUTE, 1);
        try {
            HttpPost httppost = new HttpPost(URL_END_POINT);
            String xml = String.format(BASE_XML_REQUEST,
                    ID_CLIENTE, EMAIL, PASSWORD, telefono,
                    mensaje,sdf.format(fecha.getTime()),
                    telefono);
            System.out.println("--------REQUEST------");
            System.out.println(xml);
            httppost.setEntity(new StringEntity(xml));
            httppost.setHeader("Content-Type", "text/xml;charset=utf-8");
            httppost.setHeader("SOAPAction", URL_END_POINT+"?wsdl#EnviaMensajeOL");
            SmsHandler handler = new SmsHandler();
            factory.newSAXParser().parse(new DefaultHttpClient().execute(httppost).getEntity().getContent(), handler);
            System.out.println("--------RESPONSE-----");
            System.out.println(handler.getReturn());
            return handler.getReturn();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    private final String BASE_XML_REQUEST = 
        "<s:Envelope xmlns:s=\" http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "	<s:Body s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
        "		<q1:EnviaMensajeOL xmlns:q1=\"http://www.calixtaondemand.com/wsdl\">\n" +
        "			<idCliente xsi:type=\"xsd:int\">%s</idCliente>\n" +
        "			<email xsi:type=\"xsd:string\">%s</email>7\n" +
        "			<password xsi:type=\"xsd:string\">%s</password>\n" +
        "			<tipo xsi:type=\"xsd:string\">SMS</tipo>\n" +
        "			<telefono xsi:type=\"xsd:string\">%s</telefono>\n" +
        "			<mensaje xsi:type=\"xsd:string\">%s</mensaje>\n" +
        "			<idivr xsi:type=\"xsd:int\">0</idivr>\n" +
        "			<fechaInicio xsi:type=\"xsd:string\">%s</fechaInicio>\n" +
        "			<campoAux xsi:type=\"xsd:string\"/>\n" +
        "			<asunto xsi:type=\"xsd:string\"/>\n" +
        "			<url xsi:type=\"xsd:string\"/>\n" +
        "			<idApp xsi:type=\"xsd:string\">0</idApp>\n" +
        "			<token xsi:type=\"xsd:string\"/>\n" +
        "			<parametros href=\"#id1\"/>\n" +
        "		</q1:EnviaMensajeOL>\n" +
        "		<q2:Array id=\"id1\" q2:arrayType=\"q3:Parametro[]\" xmlns:q2=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
        "		xmlns:q3=\"http://www.calixtaondemand.com/wsdl\">\n" +
        "			<Item href=\"#id2\"/>\n" +
        "		</q2:Array>\n" +
        "		<q4:Parametro id=\"id2\" xsi:type=\"q4:Parametro\" xmlns:q4=\"http://www.calixtaondemand.com/wsdl\">\n" +
        "			<nombre xsi:type=\"xsd:string\">TELEFONO</nombre>\n" +
        "			<valor xsi:type=\"xsd:string\">%s</valor>\n" +
        "		</q4:Parametro>\n" +
        "	</s:Body>\n" +
        "</s:Envelope>";
}
