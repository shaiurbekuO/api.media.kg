package api.media.kg.controller;

import api.media.kg.dto.*;
import api.media.kg.dto.auth.RegistrationDTO;
import api.media.kg.dto.auth.ResetPasswordConfirmDTO;
import api.media.kg.dto.auth.ResetPasswordDTO;
import api.media.kg.dto.sms.SmsResendDto;
import api.media.kg.dto.sms.SmsVerificationDto;
import api.media.kg.enums.AppLanguage;
import api.media.kg.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for user authentication and registration")
@Slf4j
public class AuthController {

    @Autowired
    private  AuthService authService;


    @Operation(summary = "Register new user",
            description = "Creates a new user account and sends email verification")
    @PostMapping("/registration")
    public SimpleResponse registration(@Valid @RequestBody RegistrationDTO dto,
                                        @RequestHeader("Accept-Language") AppLanguage lang) {
        log.info("Registration name: {}, username: {}", dto.getName(), dto.getUsername());
        return authService.registration(dto, lang);
    }
    @Operation(summary = "Verify email",
            description = "Confirms user's email using the token from verification email")
    @GetMapping("/reg-emailVerification/{token}")
    public SimpleResponse emailVerification(@Valid @PathVariable("token") String token,
                                          @RequestParam(value = "lang", defaultValue = "EN") AppLanguage lang) {
        log.info("Email verification token: {}", token);
        return authService.registrationEmailVerification(token, lang);
    }
    @Operation(summary = "Verify SMS code",
            description = "Confirms phone number using SMS verification code")
    @PostMapping("/reg-smsVerification")
    public ProfileDTO smsVerification(@Valid @RequestBody SmsVerificationDto dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        log.info("SMS verification: {}", dto);
        return authService.registrationSmsVerification(dto, lang);
    }

    @Operation(summary = "Resend SMS verification code",
            description = "Sends a new SMS verification code")
    @PostMapping("/reg-smsVerification-resend")
    public SimpleResponse smsVerificationResend(@Valid @RequestBody SmsResendDto dto,
                                      @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        log.info("SMS verification resend: {}",dto.getPhone());
        return authService.registrationSmsVerificationResend(dto, lang);
    }
    @Operation(summary = "User login",
            description = "Authenticates user using email/phone and password")
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody LoginDTO dto,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        log.info("Login request: {}", dto.getUsername());
        return ResponseEntity.ok(authService.login(dto, lang));
    }
    @Operation(summary = "Request password reset",
            description = "Initiates password reset process (sends email or SMS)")
    @PostMapping("/reset-password")
    public SimpleResponse resetPassword(@Valid @RequestBody ResetPasswordDTO dto,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        log.info("Login request: {}", dto.getUsername());
        return authService.resetPassword(dto, lang);
    }

    @Operation(summary = "Confirm password reset",
            description = "Confirms password reset with new password")@PostMapping("/reset-password-confirm")
    public SimpleResponse resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                        @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        log.info("Login username: {}, password: {}", dto.getUsername(), dto.getPassword());
        return authService.resetPasswordConfirm(dto, lang);
    }
}
