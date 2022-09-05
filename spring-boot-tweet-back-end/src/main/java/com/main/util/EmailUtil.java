package com.main.util;

import com.main.security.model.User;
import com.microsoft.azure.servicebus.primitives.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import static com.main.constants.CommonConstant.*;

@Component
@Slf4j
public class EmailUtil {

    DecimalFormat dff = new DecimalFormat("0.00");
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public void sendEmailForCreateUserCredentials(User userDetails, String plainPass) throws MessagingException {
        String to = userDetails.getEmail().trim();
        if(!StringUtil.isNullOrEmpty(to)) {
            String subject = "Login Credential";
            String body = "User Name: "+userDetails.getUsername()+"\n"
                    +"Password: "+plainPass+"\n\n"
                    +"Please, change your default password as you log in!!";
            sendEmail(to, subject, body);
        }
    }

    public void sendEmail(String toEmail, String subject, String body)
            throws MessagingException {
        // Create an email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom(EMAIL_SEND_FORM);
        // Send mail
        try {
            getJavaMailSender().send(mailMessage);
            //System.out.println("Email sent successfully");
        } catch (MailException ex) {
            log.error("Error: {}", ex.getLocalizedMessage(), ex);
        }
    }

    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //mailSender.setHost("send.one.com");
        mailSender.setHost(EMAIL_SENDER_HOST); //outgoing / smtp server
        //mailSender.setPort(465); // throw error

//        mailSender.setUsername("postmaster@huberslaw.co.uk");
//        mailSender.setPassword("HubersPost");

        mailSender.setUsername(EMAIL_SEND_FORM);
        mailSender.setPassword(EMAIL_SEND_PASS);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
