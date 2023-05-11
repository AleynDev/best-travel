package com.aleyn.best_travel.infrastructure.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class EmailHelper {

    private final JavaMailSender mailSender;

    public void sendMail(String to, String name, String product) {
        MimeMessage message = mailSender.createMimeMessage();
        String htmlContent = this.readHtmlTemplate(name, product);

        try {
            message.setFrom(new InternetAddress("aleyncoello5@gmail.com"));
            message.setSubject("Confirmation of " + product);
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error to send mail", e);
        }
    }

    private String readHtmlTemplate(String name, String product) {
        try (var lines = Files.lines(MAIL_TEMPLATE_PATH)) {
            var html = lines.collect(Collectors.joining());
            return html.replace("{name}", name).replace("{product}", product);
        } catch (IOException e) {
            log.error("Could not read template html", e);
            throw new RuntimeException();
        }
    }

    private final Path MAIL_TEMPLATE_PATH = Paths.get("src/main/resources/email/email_template.html");

}
