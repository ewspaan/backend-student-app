package nl.spaan.student_app.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailServiceImpl implements EmailService{

    private String host;
    private int port;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Long houseId;


    public EmailServiceImpl(String host, int port, String username, String password, String firstName, String lastName, String email, Long houseId) {

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.houseId = houseId;

        sendMail();
    }
    //Maak body voor email met daar inde informatie om je aan te melden voor het huis als huisgenoot.
    @Override
    public String CreateEmailBody(){
        String message;

        //TODO volledige naam toevoegen
        if( firstName == null) {
            message = "<i>Hallo Student</i><br>";
        }else{
            message = "<i>Hallo " +  firstName + "</i><br>";
        }
        message += "<b>Welkom in dit huis.</b><br>";
        message += "meld je aan via deze link<br>";
        message += "<a href='http://localhost:3000/huisgenoot/signup/"
                +firstName+ "/"
                +lastName+"/"
                +email+"/"
                +houseId+"'>Huis.frl</a><br>";
        message += "<font color=red>Huisoudste</font>";

        System.out.println("emailBody-->  " + message);

        return message;
    }

    @Override
    public void sendMail() {

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("StudentApp@Student.app"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Aanmelden Als Huisgenoot van Huize");

            String msg = CreateEmailBody();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File("pom.xml"));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



