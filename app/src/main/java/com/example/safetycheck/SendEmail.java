package com.example.safetycheck;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {
    private final String email = "your_email_address";
    private final String password = "your_email_password";
    private final String recipient = "recipient_email_address";
    private final String subject = "Subject of the email";
    private final String message = "Message body of the email";

    public void send() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress(email));
        emailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        Transport.send(emailMessage);
    }
}
