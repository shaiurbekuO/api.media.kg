package api.media.kg.controller;

import api.media.kg.dto.*;
import api.media.kg.dto.sms.SmsRequestDto;
import api.media.kg.dto.sms.SmsSendResponseDto;
import api.media.kg.entity.SmsHistoryEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.SmsType;
import api.media.kg.exception.BadRequestException;
import api.media.kg.service.AuthService;
import api.media.kg.service.SmsSendService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final SmsSendService smsSendService;
    private final AuthService authService;

    public AuthController(SmsSendService smsSendService, AuthService authService) {
        this.smsSendService = smsSendService;
        this.authService = authService;
    }

    @PostMapping("/registration")
    public SimpleResponse registration(@Valid @RequestBody RegistrationDTO registration,
                                        @RequestHeader("Accept-Language") AppLanguage lang) {
        return authService.registration(registration, lang);
    }
    @GetMapping("/reg-validation/{token}")
    public SimpleResponse emailValidation(@PathVariable("token") String token,
                                          @RequestParam(value = "lang", defaultValue = "KG") AppLanguage lang) {
        return authService.registrationEmailValidation(token, lang);
    }
    
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody LoginDTO loginDTO,
                                            @RequestHeader("Accept-Language") AppLanguage lang) {
        return ResponseEntity.ok(authService.login(loginDTO, lang));
    }

//    @GetMapping("/token")
//    public String getToken() {
//        return smsSendService.getToken();
//    }
//
//    @PostMapping("/send-sms")
//    public SmsSendResponseDto sendSms(
//            @Valid @RequestBody SmsRequestDto sendSmsDTO,
//            @RequestParam SmsType smsType
//    ) {
//        String code = generateCode();
//        return smsSendService.sendSms(sendSmsDTO.getMobile_phone(), sendSmsDTO.getMessage(), code, smsType);
//    }


}
