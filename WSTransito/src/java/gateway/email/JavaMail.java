/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.email;


import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Frost
 */
public class JavaMail {
    private static final String USERNAME = "pruebas@hightek.com.mx";
    private static final String PASSWORD = "P@55word";
    private static final String SMTP_AUTH = "true";
    private static final String TTLS_ENABLE = "true";
    private static final String SMTP_HOST = "hightek.com.mx";
    private static final String SMTP_PORT = "587";
    private static final String FROM = "pruebas@hightek.com.mx";
    
    public static boolean send(String account, String subject, String messageText){
        final String username= USERNAME;
        final String password= PASSWORD;
        
        // SESIÓN DE SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", TTLS_ENABLE);
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
        
        try{
            // CREAR EL CORREO ELECTRÓNICO 
            final MimeMessage message= new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account));
            message.setSubject(subject);
            message.setText(messageText);
            
            // AGREGAR ELEMENTOS EXTRAS AL CORREO (TEXTO FORMATEADO / ARCHIVOS ADJUNTOS)
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageText, "text/html");
            Multipart multipart= new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            
            Transport.send(message);
            return true;
        }catch(MessagingException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean sendWithAttachment(String account, String subject, 
            String messageText, String[] attachFiles) throws IOException{
        final String username= USERNAME;
        final String password= PASSWORD;
        
        // SESIÓN DE SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", TTLS_ENABLE);
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
        
        try{
            // CREAR EL CORREO ELECTRÓNICO 
            final MimeMessage message= new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account));
            message.setSubject(subject);
            message.setText(messageText);
            
            // AGREGAR ELEMENTOS EXTRAS AL CORREO (TEXTO FORMATEADO / ARCHIVOS ADJUNTOS)
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageText, "text/html");
            Multipart multipart= new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            if (attachFiles != null && attachFiles.length>0){
                for (String filePath :attachFiles){
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try{
                        attachPart.attachFile(filePath);
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                    multipart.addBodyPart(attachPart);
                }
            }
            message.setContent(multipart);
            
            Transport.send(message);
            return true;
        }catch(MessagingException e){
            e.printStackTrace();
        }
        return false;
    }
    
}