package api.media.kg.service;

import api.media.kg.entity.SmsHistoryEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.SmsType;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.SmsHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsHistoryService {
    private final SmsHistoryRepository smsHistoryRepository;
    private final ResourceBundleService bundleService;

    public SmsHistoryService(SmsHistoryRepository smsHistoryRepository, ResourceBundleService bundleService) {
        this.smsHistoryRepository = smsHistoryRepository;
        this.bundleService = bundleService;
    }

    public void create(String phoneNumber, String message, String code, SmsType smsType) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhone(phoneNumber);
        entity.setMessage(message);
        entity.setCode(code);
        entity.setSmsType(smsType);
        entity.setAttemptCount(0);
        entity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(entity);
    }

    public Long getSmsCount(String phone) {
        LocalDateTime now = LocalDateTime.now();
        return smsHistoryRepository.countByPhoneAndCreatedDateBetween(phone, now.minusMinutes(2), now);
    }

    public boolean check(String phoneNumber, String code, AppLanguage lang) {
        //* 1. Находим последнее SMS
        Optional<SmsHistoryEntity> optional = smsHistoryRepository.findTop1ByPhoneOrderByCreatedDateDesc(phoneNumber);
        if(optional.isEmpty()) {
            throw new BadRequestException(bundleService.getMessage("verification.failed", lang));
        }

        SmsHistoryEntity entity = optional.get();
//        * attempt Count check
        if(entity.getAttemptCount() >= 3) {
            throw new BadRequestException(bundleService.getMessage("attempt.count.exceeded.limit", lang));
        }
        //* 2. Проверяем совпадение кода
        if(!entity.getCode().equals(code)) {
            smsHistoryRepository.updateAttemptCount(entity.getId());
            throw new BadRequestException(bundleService.getMessage("verification.failed", lang));
        }

        //* 3. Проверяем время действия кода
        LocalDateTime expDate = entity.getCreatedDate().plusMinutes(2);
        if(LocalDateTime.now().isAfter(expDate)) {
            throw new BadRequestException(bundleService.getMessage("code.expired", lang));
        }
        return true;
    }
}