package api.media.kg.service;

import api.media.kg.entity.EmailHistoryEntity;
import api.media.kg.entity.SmsHistoryEntity;
import api.media.kg.enums.SmsType;
import api.media.kg.repository.EmailHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailHistoryService {
    private final EmailHistoryRepository emailHistoryRepository;

    public EmailHistoryService(EmailHistoryRepository emailHistoryRepository) {
        this.emailHistoryRepository = emailHistoryRepository;
    }
    public void create(String phoneNumber, String message, String code, SmsType smsType) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setPhone(phoneNumber);
        entity.setMessage(message);
        entity.setCode(code);
        entity.setSmsType(smsType);
        entity.setAttemptCount(0);
        entity.setCreatedDate(LocalDateTime.now());
        emailHistoryRepository.save(entity);
    }
    public Long getEmailCount(String phone) {
        LocalDateTime now = LocalDateTime.now();
        return emailHistoryRepository.countByPhoneAndCreatedDateBetween(phone, now.minusMinutes(2), now);
    }
}
