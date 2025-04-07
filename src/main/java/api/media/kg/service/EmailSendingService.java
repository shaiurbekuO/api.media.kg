package api.media.kg.service;

import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.SmsType;
import api.media.kg.exception.BadRequestException;
import api.media.kg.util.JwtUtil;
import api.media.kg.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailSendingService {
    @Value("${spring.mail.username}")
    private String fromAccount;
    private final JavaMailSender javaMailSender;
    private final EmailHistoryService emailHistoryService;
    private final Integer limit = 3;

    public EmailSendingService(JavaMailSender javaMailSender, EmailHistoryService emailHistoryService) {
        this.javaMailSender = javaMailSender;
        this.emailHistoryService = emailHistoryService;
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
    public void sendResetPasswordEmail(String email, AppLanguage lang) {
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
        messages.put(AppLanguage.KG, "Сырсөздү ырастоо үчүн төмөнкү код колдонуңуз: %s");
        messages.put(AppLanguage.RU, "Используйте следующий код для подтверждения сброса пароля: %s");
        messages.put(AppLanguage.EN, "Use the following code to confirm your password reset: %s");

        // Шилтеме текстин аныктайбыз
        Map<AppLanguage, String> linkTexts = new HashMap<>();
        linkTexts.put(AppLanguage.RU, "Подтвердить сброс пароля");
        linkTexts.put(AppLanguage.EN, "Confirm Password Reset");

        // Тилге жараша текстти тандайбыз
        String subject = subjects.getOrDefault(lang, subjects.get(AppLanguage.KG));
        String greeting = greetings.getOrDefault(lang, greetings.get(AppLanguage.KG));
        String messageTemplate = messages.getOrDefault(lang, messages.get(AppLanguage.KG));
        String message = String.format(messageTemplate, code);

        String body = String.format("<!DOCTYPE html>\n" +
                        "<html lang=\"%s\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>%s</title>\n" +
                        "    <style>\n" +
                        "        .code {\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: bold;\n" +
                        "            background-color: #f0f0f0;\n" +
                        "            padding: 10px;\n" +
                        "            border-radius: 5px;\n" +
                        "            margin: 10px 0;\n" +
                        "            display: inline-block;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1 style=\"text-align: center\">%s</h1>\n" +
                        "<p>%s</p>\n" +
                        "<p>%s</p>\n" +
                        "<p class=\"code\">%s</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>",
                lang.toString().toLowerCase(), // html тили
                subject, // title
                subject, // h1 тексти
                greeting, // саламдашуу
                message, // негизги билдирүү (кодду камтыйт)
                code     // кодду өзүнчө көрсөтүү
        );
        checkAndSendMimeEmail(email, subject, body, code);
    }
    public void sendChangeUsernameEmail(String email, AppLanguage lang) {
        String code = RandomUtil.getRandomSmsCode();
        // Темаларды (subjects) аныктайбыз
        Map<AppLanguage, String> subjects = new HashMap<>();
        subjects.put(AppLanguage.KG, "Сырсөздү ырастоо");
        subjects.put(AppLanguage.RU, "Подтверждение сброса пароля");
        subjects.put(AppLanguage.EN, "Username Change Confirmation");

        // Саламдашууларды аныктайбыз
        Map<AppLanguage, String> greetings = new HashMap<>();
        greetings.put(AppLanguage.KG, "Салам, кандайсыз?");
        greetings.put(AppLanguage.RU, "Здравствуйте, как Вы?");
        greetings.put(AppLanguage.EN, "Hello, how are you?");

        // Текстти аныктайбыз
        Map<AppLanguage, String> messages = new HashMap<>();
        messages.put(AppLanguage.KG, "Сырсөздү ырастоо үчүн төмөнкү код колдонуңуз: %s");
        messages.put(AppLanguage.RU, "Используйте следующий код для подтверждения сброса пароля: %s");
        messages.put(AppLanguage.EN, "Use the following code to confirm your password reset: %s");

        // Шилтеме текстин аныктайбыз
        Map<AppLanguage, String> linkTexts = new HashMap<>();
        linkTexts.put(AppLanguage.KG, "Колдонуучунун атын өзгөртүү ырастоо");
        linkTexts.put(AppLanguage.RU, "Подтверждение изменения имени пользователя");
        linkTexts.put(AppLanguage.EN, "Username Change Confirmation");

        // Тилге жараша текстти тандайбыз
        String subject = subjects.getOrDefault(lang, subjects.get(AppLanguage.KG));
        String greeting = greetings.getOrDefault(lang, greetings.get(AppLanguage.KG));
        String messageTemplate = messages.getOrDefault(lang, messages.get(AppLanguage.KG));
        String message = String.format(messageTemplate, code);

        String body = String.format("<!DOCTYPE html>\n" +
                        "<html lang=\"%s\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>%s</title>\n" +
                        "    <style>\n" +
                        "        .code {\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: bold;\n" +
                        "            background-color: #f0f0f0;\n" +
                        "            padding: 10px;\n" +
                        "            border-radius: 5px;\n" +
                        "            margin: 10px 0;\n" +
                        "            display: inline-block;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1 style=\"text-align: center\">%s</h1>\n" +
                        "<p>%s</p>\n" +
                        "<p>%s</p>\n" +
                        "<p class=\"code\">%s</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>",
                lang.toString().toLowerCase(), // html тили
                subject, // title
                subject, // h1 тексти
                greeting, // саламдашуу
                message, // негизги билдирүү (кодду камтыйт)
                code     // кодду өзүнчө көрсөтүү
        );
        checkAndSendMimeEmail(email, subject, body, code);
    }
    private void checkAndSendMimeEmail(String email, String subject, String body, String code) {
        //        *check
        Long count = emailHistoryService.getEmailCount(email);
        if(count >= limit) {
            log.info("Email limit reached: {}", count);
            throw new BadRequestException("Email limit reached");
        }
        //        * send
        sendMimeEmail(email, subject, body);
        //        * create
        emailHistoryService.create(email, code, SmsType.CHANGE_USERNAME_CONFIRM);
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
            log.error("Email sending failed: {}", email);
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
