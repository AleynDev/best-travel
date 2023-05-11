//package com.aleyn.best_travel.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//

// Example class to configure email, already configured in application.properties

//@Configuration
//public class EmailConfig {
//
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("best_travel");
//        mailSender.setPassword("private");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttis.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//
//}
