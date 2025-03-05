package api.media.kg.controller;

import api.media.kg.dto.RegistrationDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.exception.BadRequestException;
import api.media.kg.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public SimpleResponse registration(@Valid @RequestBody RegistrationDTO registration) throws BadRequestException {
        authService.registration(registration);
        return new SimpleResponse(HttpStatus.OK, "Registration successful");
    }
}
