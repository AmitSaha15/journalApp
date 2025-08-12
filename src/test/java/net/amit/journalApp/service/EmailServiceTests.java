package net.amit.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendMailTest(){
        String subject = "Welcome to Journal App";
        String body = "Write your everyday journal here!";
        emailService.sendMail("amitsahawork42@gmail.com", subject, body);
    }
}
