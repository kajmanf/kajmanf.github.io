package com.example.winninglog.Services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailSenderService implements EmailService{

    /*
    @Autowired
    private JavaMailSender mailSender;
     */
    @Autowired
    private Environment env;

    Dotenv dotenv = Dotenv.load();

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", env.getProperty("spring.mail.host"));
        properties.put("mail.smtp.port", env.getProperty("spring.mail.port"));
        properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
        properties.put("mail.smtp.ssl.trust", env.getProperty("spring.mail.properties.mail.smtp.ssl.trust"));

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                    }
                });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("winninglog@gmail.com"));
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmail));
        message.setContent("<h3>To reset password folow this link:</h3><br>" + body, "text/html");
        message.setSubject(subject);

        Transport.send(message);

        /*
        //send just text
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("winninglog@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
         */

        System.out.println("Email was sent");
    };

    @Override
    public void sendRetrievalMail(String retrieval, String mailTo) {
        try{
            String url = dotenv.get("DATABASE_ADRESS") + "changepassword/" + retrieval;
            sendEmail(
                    mailTo,
                    "Password retrieval",
                    "<a href=\"" + url + "\"></a>");
            System.out.println("I got here");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
