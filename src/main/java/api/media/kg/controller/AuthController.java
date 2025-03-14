package api.media.kg.controller;

import api.media.kg.dto.*;
import api.media.kg.enums.AppLanguage;
import api.media.kg.exception.BadRequestException;
import api.media.kg.service.AuthService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
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
}
