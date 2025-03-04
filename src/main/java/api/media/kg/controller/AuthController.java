package api.media.kg.controller;

import api.media.kg.dto.RegistrationDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public SimpleResponse registration(@RequestBody RegistrationDTO registration) {
            authService.Registration(registration);
            return new SimpleResponse(HttpStatus.OK, "Success");
    }
}
