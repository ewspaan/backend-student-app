package nl.spaan.student_app.service;

import nl.spaan.student_app.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService{


    @Value("${spaan.sec.emailHost}")
    private String emailHost;

    @Value("${spaan.sec.emailPort}")
    private int emailPort;

    @Value("${spaan.sec.emailUserName}")
    private String emailUserName;

    @Value("${spaan.sec.emailPassword}")
    private String emailPassword;

    @Value("${spaan.sec.useEmail}")
    private boolean useEmail;


    //Maak body voor email met daar inde informatie om je aan te melden voor het huis als huisgenoot.
    private String CreateEmailBody(User user){

        String message;
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        long houseId = user.getHouse().getId();

        //TODO volledige naam toevoegen
        if( firstName == null) {
            message = "<i>Hallo Student</i><br>";
            firstName = "voornaam";
        }else{
            message = "<i>Hallo " +  firstName + "</i><br>";
        }
        if( lastName == null){
            lastName = "achternaam";
        }
        message += "<b>Welkom in dit huis.</b><br>";
        message += "meld je aan via deze link<br>";
        message += "<a href='http://localhost:3000/huisgenoot/signup/"
                +firstName+ "/"
                +lastName+"/"
                +email+"/"
                +houseId+"'>Huis.frl</a><br>";
        message += "<font color=red>Huisoudste</font>";

        return message;
    }

    @Override
    public void sendMail(User user) {

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", emailHost);
        prop.put("mail.smtp.port", emailPort);
        prop.put("mail.smtp.ssl.trust", emailHost);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUserName, emailPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("StudentApp@Student.app"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Aanmelden Als Huisgenoot van Huize");

            String msg = CreateEmailBody(user);


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File("pom.xml"));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            if(!useEmail){
                System.out.println("emailBody-->  " + msg);
            }
            else {
                Transport.send(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



