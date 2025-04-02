package api.media.kg.service;


import api.media.kg.dto.sms.SmsAuthDto;
import api.media.kg.dto.sms.SmsAuthResponseDto;
import api.media.kg.dto.sms.SmsRequestDto;
import api.media.kg.dto.sms.SmsSendResponseDto;
import api.media.kg.entity.SmsProviderHolderEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.SmsType;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.SmsProviderHolderRepository;
import api.media.kg.util.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SmsSendService {
    private final RestTemplate restTemplate;
    private final SmsProviderHolderRepository smsProviderHolderRepository;
    private final SmsHistoryService smsHistoryService;
    @Value("${eskiz.url}")
    private String smsURL;
    @Value("${eskiz.login}")
    private String accountLogin;
    @Value("${eskiz.password}")
    private String accountPassword;
    @Value("${sms.limit}")
    private Integer limit;

    public SmsSendService(RestTemplate restTemplate, SmsProviderHolderRepository smsProviderHolderRepository, SmsHistoryService smsHistoryService) {
        this.restTemplate = restTemplate;
        this.smsProviderHolderRepository = smsProviderHolderRepository;
        this.smsHistoryService = smsHistoryService;
    }
    public void sendRegistrationSms(String phoneNumber) {
        String code = RandomUtil.getRandomSmsCode();
        String message = "Bu Eskiz dan test";
        message = String.format(message, code);
        sendSms(phoneNumber, message, code, SmsType.REGISTRATION);
    }
    public void sendResetPasswordSms(String phoneNumber) {
        String code = RandomUtil.getRandomSmsCode();
        String message = "Bu Eskiz dan test";
        message = String.format(message, code);
        sendSms(phoneNumber, message, code, SmsType.RESET_PASSWORD);
    }
    private SmsSendResponseDto sendSms(String phoneNumber, String message, String code, SmsType smsType) {
//*     check sms limit
        Long count = smsHistoryService.getSmsCount(phoneNumber);
        if(count >= limit) throw new BadRequestException("Sms limit reached");
//*     send
        SmsSendResponseDto result = sendSms(phoneNumber, message);
//*     save
        smsHistoryService.create(phoneNumber, message, code, smsType);
        return result;
    }
    private SmsSendResponseDto sendSms(String phoneNumber, String message) {
        String token = getToken();

        //* Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        //* Body
        SmsRequestDto body = new SmsRequestDto();
        body.setMobile_phone(phoneNumber);
        body.setMessage(message);
        body.setFrom("4546");

        //* Send request
        try {
            HttpEntity<SmsRequestDto> entity = new HttpEntity<>(body, headers);
            ResponseEntity<SmsSendResponseDto> response = restTemplate.exchange(
                    smsURL + "/message/sms/send",
                    HttpMethod.POST,
                    entity,
                    SmsSendResponseDto.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new BadRequestException("Серверден ката келди: " + response.getBody());
            }
        } catch (RuntimeException e) {
            throw new BadRequestException("СМС жөнөтүү убагында ката кетти: " + e.getMessage());
        }
    }
    private String getToken() {
        Optional<SmsProviderHolderEntity> optional = smsProviderHolderRepository.findTop1By();
        if (optional.isEmpty()) {
            String token = getTokenFromProvider();
            SmsProviderHolderEntity smsProviderHolderEntity = new SmsProviderHolderEntity();
            smsProviderHolderEntity.setToken(token);
            smsProviderHolderEntity.setCreatedDate(LocalDateTime.now());
            smsProviderHolderEntity.setExpirationDate(LocalDateTime.now().plusMonths(1));
            smsProviderHolderRepository.save(smsProviderHolderEntity);
            return token;
        }

        SmsProviderHolderEntity entity = optional.get();
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(entity.getExpirationDate())) {
            return entity.getToken();
        }

        String token = getTokenFromProvider();
        entity.setToken(token);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setExpirationDate(LocalDateTime.now().plusMonths(1)); // Жаңы мөөнөт коюу
        smsProviderHolderRepository.save(entity);
        return token;
    }
    private String getTokenFromProvider()  {
        SmsAuthDto smsAuthDto = new SmsAuthDto();
        smsAuthDto.setEmail(accountLogin);
        smsAuthDto.setPassword(accountPassword);

       try {
           SmsAuthResponseDto responseDto = restTemplate.postForObject(smsURL+"/auth/login",smsAuthDto, SmsAuthResponseDto.class);
           return responseDto.getData().getToken();
       }catch (Exception e){
           throw new RuntimeException(e);
       }

    }
}
