package api.media.kg.controller;

import api.media.kg.dto.*;
import api.media.kg.exception.BadRequestException;
import api.media.kg.service.AuthService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:63342") // Фронтенд домени
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public SimpleResponse registration(@Valid @RequestBody RegistrationDTO registration) {
        return authService.registration(registration);
    }
    @GetMapping("/reg-validation/{token}")
    public SimpleResponse regValidation(@PathVariable("token") String token) {
        return authService.regValidation(token);
    }
    
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }
}
