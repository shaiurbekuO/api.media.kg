package api.media.kg.service;

import api.media.kg.enums.AppLanguage;
import api.media.kg.util.JwtUtil;
import api.media.kg.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
        String token = JwtUtil.encode(profileId);

        // Темаларды (subjects) аныктайбыз
        Map<AppLanguage, String> subjects = new HashMap<>();
        subjects.put(AppLanguage.KG, "Каттоону бүтүрүү");
        subjects.put(AppLanguage.RU, "Завершение регистрации");
        subjects.put(AppLanguage.EN, "Complete Registration");

        // Саламдашууларды аныктайбыз
        Map<AppLanguage, String> greetings = new HashMap<>();
        greetings.put(AppLanguage.KG, "Салам, кандайсыз?");
        greetings.put(AppLanguage.RU, "Здравствуйте, как Вы?");
        greetings.put(AppLanguage.EN, "Hello, how are you?");

        // Текстти аныктайбыз
        Map<AppLanguage, String> messages = new HashMap<>();
        messages.put(AppLanguage.KG, "Каттоону бүтүрүү үчүн төмөнкү шилтемени басыңыз:");
        messages.put(AppLanguage.RU, "Нажмите на ссылку ниже, чтобы завершить регистрацию:");
        messages.put(AppLanguage.EN, "Click the link below to complete registration:");

        // Шилтеме текстин аныктайбыз
        Map<AppLanguage, String> linkTexts = new HashMap<>();
        linkTexts.put(AppLanguage.KG, "Бул жерди басыңыз");
        linkTexts.put(AppLanguage.RU, "Нажмите здесь");
        linkTexts.put(AppLanguage.EN, "Click here");

        // Тилге жараша текстти тандайбыз
        String subject = subjects.getOrDefault(lang, subjects.get(AppLanguage.KG));
        String greeting = greetings.getOrDefault(lang, greetings.get(AppLanguage.KG));
        String message = messages.getOrDefault(lang, messages.get(AppLanguage.KG));
        String linkText = linkTexts.getOrDefault(lang, linkTexts.get(AppLanguage.KG));

        String body = String.format("<!DOCTYPE html>\n" +
                        "<html lang=\"%s\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>%s</title>\n" +
                        "    <style>\n" +
                        "        .link {\n" +
                        "            color: blue;\n" +
                        "            text-decoration: underline;\n" +
                        "        }\n" +
                        "\n" +
                        "        .link:hover {\n" +
                        "            color: darkblue;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1 style=\"text-align: center\">%s</h1>\n" +
                        "<p>%s</p>\n" +
                        "<p>\n" +
                        "    %s <a class=\"link\"\n" +
                        "           href=\"http://localhost:8080/api/auth/reg-emailVerification/%s?lang=%s\"\n" +
                        "           target=\"_blank\">%s</a>\n" +
                        "</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>",
                lang.toString().toLowerCase(), // html тили
                subject, // title
                subject, // h1 тексти
                greeting, // саламдашуу
                message, // негизги билдирүү
                token, // токен
                lang, // тил параметри
                linkText // шилтеме тексти
        );

        sendMimeEmail(email, subject, body);
    }
    public void sendResetPasswordEmail(String email,  AppLanguage lang) {
        String code = RandomUtil.getRandomSmsCode();
        // Темаларды (subjects) аныктайбыз
        Map<AppLanguage, String> subjects = new HashMap<>();
        subjects.put(AppLanguage.KG, "Сырсөздү ырастоо");
        subjects.put(AppLanguage.RU, "Подтверждение сброса пароля");
        subjects.put(AppLanguage.EN, "Reset Password Confirmation");

        // Саламдашууларды аныктайбыз
        Map<AppLanguage, String> greetings = new HashMap<>();
        greetings.put(AppLanguage.KG, "Салам, кандайсыз?");
        greetings.put(AppLanguage.RU, "Здравствуйте, как Вы?");
        greetings.put(AppLanguage.EN, "Hello, how are you?");

        // Текстти аныктайбыз
        Map<AppLanguage, String> messages = new HashMap<>();
        messages.put(AppLanguage.KG, "Сырсөздү ырастоо үчүн төмөнкү шилтемени басыңыз:");
        messages.put(AppLanguage.RU, "Нажмите на ссылку ниже, чтобы подтвердить свой пароль.:");
        messages.put(AppLanguage.EN, "Click the link below to confirm your password.:");

        // Шилтеме текстин аныктайбыз
        Map<AppLanguage, String> linkTexts = new HashMap<>();
        linkTexts.put(AppLanguage.KG, "Бул жерди басыңыз");
        linkTexts.put(AppLanguage.RU, "Нажмите здесь");
        linkTexts.put(AppLanguage.EN, "Click here");

        // Тилге жараша текстти тандайбыз
        String subject = subjects.getOrDefault(lang, subjects.get(AppLanguage.KG));
        String greeting = greetings.getOrDefault(lang, greetings.get(AppLanguage.KG));
        String message = messages.getOrDefault(lang, messages.get(AppLanguage.KG));
        String linkText = linkTexts.getOrDefault(lang, linkTexts.get(AppLanguage.KG));

        String body = String.format("<!DOCTYPE html>\n" +
                        "<html lang=\"%s\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>%s</title>\n" +
                        "    <style>\n" +
                        "        .link {\n" +
                        "            color: blue;\n" +
                        "            text-decoration: underline;\n" +
                        "        }\n" +
                        "\n" +
                        "        .link:hover {\n" +
                        "            color: darkblue;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1 style=\"text-align: center\">%s</h1>\n" +
                        "<p>%s</p>\n" +
                        "<p>\n" +
                        "    %s <a class=\"link\"\n" +
                        "           href=\"http://localhost:8080/api/auth/reg-emailVerification/lang=%s\"\n" +
                        "           target=\"_blank\">%s</a>\n" +
                        "</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>",
                lang.toString().toLowerCase(), // html тили
                subject, // title
                subject, // h1 тексти
                greeting, // саламдашуу
                message, // негизги билдирүү
                lang, // тил параметри
                linkText // шилтеме тексти
        );

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
