package com.example.Event.Management.System.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    // Mail sender 
    public void sendRegistrationMail(String to, String eventName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Event Registration Successful");
        message.setText(
                "Hello,\n\n" +
                "You have successfully registered for the event: " + eventName + "\n" +
                "We look forward to seeing you there.\n\n" +
                "Regards,\nEvent Management System"
        );

        mailSender.send(message);
    }
}
