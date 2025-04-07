package api.media.kg.service;

import api.media.kg.entity.EmailHistoryEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.SmsType;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.EmailHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class EmailHistoryService {
    private final EmailHistoryRepository emailHistoryRepository;
    private final ResourceBundleService bundleService;


    public EmailHistoryService(EmailHistoryRepository emailHistoryRepository, ResourceBundleService bundleService) {
        this.emailHistoryRepository = emailHistoryRepository;
        this.bundleService = bundleService;
    }
    public void create(String email, String code, SmsType emailType) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setCode(code);
        entity.setEmailType(emailType);
        entity.setAttemptCount(0);
        entity.setCreatedDate(LocalDateTime.now());
        emailHistoryRepository.save(entity);
    }
    public Long getEmailCount(String email) {
        LocalDateTime now = LocalDateTime.now();
        return emailHistoryRepository.countByEmailAndCreatedDateBetween(email, now.minusMinutes(2), now);
    }

    public boolean check(String email, String code, AppLanguage lang) {
        //* 1. Находим последнее SMS
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findTop1ByEmailOrderByCreatedDateDesc(email);
        if(optional.isEmpty()) {
            log.warn("Validation email failed: {}", email);
            throw new BadRequestException(bundleService.getMessage("verification.failed", lang));
        }

        EmailHistoryEntity entity = optional.get();
//        * attempt Count check
        if(entity.getAttemptCount() >= 3) {
            log.warn("Attempt count exceeded limit {}", email);
            throw new BadRequestException(bundleService.getMessage("attempt.count.exceeded.limit", lang));
        }
        //* 2. Проверяем совпадение кода
        if(!entity.getCode().equals(code)) {
            emailHistoryRepository.updateAttemptCount(entity.getId());
            log.warn("Validation email failed: {}", email);
            throw new BadRequestException(bundleService.getMessage("verification.failed", lang));
        }

        //* 3. Проверяем время действия кода
        LocalDateTime expDate = entity.getCreatedDate().plusMinutes(2);
        if(LocalDateTime.now().isAfter(expDate)) {
            log.warn("Sms expired {}", email);
            throw new BadRequestException(bundleService.getMessage("code.expired", lang));
        }
        return true;
    }
}
