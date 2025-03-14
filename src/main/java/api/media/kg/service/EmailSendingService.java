package api.media.kg.service;

import api.media.kg.enums.AppLanguage;
import api.media.kg.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailSendingService {
    @Value("${spring.mail.username}")
    private String fromAccount;
    private final JavaMailSender javaMailSender;

    public EmailSendingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendRegistrationEmail(String email, Long profileId, AppLanguage lang) {
        String subject = "Каттоону бүтүрүү";
        String token = JwtUtil.encode(profileId);
        String body = String.format("<!DOCTYPE html>\n" +
                "<html lang=\"ky\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Каттоону бүтүрүү</title>\n" +
                "    <style>\n" +
                "        .link {\n" +
                "            color: blue; /* көк түс */\n" +
                "            text-decoration: underline; /* сызык кошуу */\n" +
                "        }\n" +
                "\n" +
                "        .link:hover {\n" +
                "            color: darkblue; /* үстүнө чыкканда түсү өзгөрөт */\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1 style=\"text-align: center\">Каттоону бүтүрүү</h1>\n" +
                "<p>Салам, кандайсыз?</p>\n" +
                "<p>\n" +
                "    Каттоону бүтүрүү үчүн төмөнкү шилтемени басыңыз: <a class=\"link\"\n" +
                "                                                           href=\"http://localhost:8080/api/auth/reg-validation/%s?lang=%s\"\n" +
                "                                                           target=\"_blank\">Бул жерди басыңыз</a>\n" +
                "</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>", token, lang);

        sendMimeEmail(email, subject, body);
    }
    private void sendMimeEmail(String email, String subject, String body) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            CompletableFuture.runAsync(()->{
                javaMailSender.send(msg);
            });
        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private void sendSimpleEmail(String email, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }
}
