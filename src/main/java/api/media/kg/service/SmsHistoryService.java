package api.media.kg.service;

import api.media.kg.entity.SmsHistoryEntity;
import api.media.kg.enums.SmsType;
import api.media.kg.repository.SmsHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsHistoryService {
  private final SmsHistoryRepository smsHistoryRepository;

    public SmsHistoryService(SmsHistoryRepository smsHistoryRepository) {
        this.smsHistoryRepository = smsHistoryRepository;
    }

    public void create(String phoneNumber, String message, String code, SmsType smsType) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhone(phoneNumber);
        entity.setMessage(message);
        entity.setCode(code);
        entity.setSmsType(smsType);
        entity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(entity);
    }

    public Long getSmsCount(String phone) {
        LocalDateTime now = LocalDateTime.now();
        return smsHistoryRepository.countByPhoneAndCreatedDateBetween(phone, now.minusMinutes(5), now);
    }
}