package com.tweetapp.util;

import com.tweetapp.constants.Constants;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import java.util.Properties;

public class EmailUtility {

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom(Constants.EMAIL_SEND_FORM);

        try {
            getJavaMailSender().send(mailMessage);
            //System.out.println("Email sent successfully");
        } catch (MailException ex) {
            System.out.println(ex);
        }
    }

    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(Constants.EMAIL_SENDER_HOST);

        mailSender.setUsername(Constants.EMAIL_SEND_FORM);
        mailSender.setPassword(Constants.EMAIL_SEND_PASS);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
