package api.media.kg.controller;

import api.media.kg.dto.*;
import api.media.kg.dto.auth.RegistrationDTO;
import api.media.kg.dto.auth.ResetPasswordConfirmDTO;
import api.media.kg.dto.auth.ResetPasswordDTO;
import api.media.kg.dto.sms.SmsResendDto;
import api.media.kg.dto.sms.SmsVerificationDto;
import api.media.kg.enums.AppLanguage;
import api.media.kg.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public SimpleResponse registration(@Valid @RequestBody RegistrationDTO registration,
                                        @RequestHeader("Accept-Language") AppLanguage lang) {
        return authService.registration(registration, lang);
    }
    @GetMapping("/reg-emailVerification/{token}")
    public SimpleResponse emailVerification(@Valid @PathVariable("token") String token,
                                          @RequestParam(value = "lang", defaultValue = "EN") AppLanguage lang) {
        return authService.registrationEmailVerification(token, lang);
    }
    @PostMapping("/reg-smsVerification")
    public ProfileDTO smsVerification(@Valid @RequestBody SmsVerificationDto dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return authService.registrationSmsVerification(dto, lang);
    }

    @PostMapping("/reg-smsVerification-resend")
    public SimpleResponse smsVerificationResend(@Valid @RequestBody SmsResendDto dto,
                                      @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return authService.registrationSmsVerificationResend(dto, lang);
    }
    
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody LoginDTO loginDTO,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return ResponseEntity.ok(authService.login(loginDTO, lang));
    }
    @PostMapping("/reset-password")
    public SimpleResponse resetPassword(@Valid @RequestBody ResetPasswordDTO dto,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return authService.resetPassword(dto, lang);
    }
    @PostMapping("/reset-password-confirm")
    public SimpleResponse resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                        @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return authService.resetPasswordConfirm(dto, lang);
    }


}
