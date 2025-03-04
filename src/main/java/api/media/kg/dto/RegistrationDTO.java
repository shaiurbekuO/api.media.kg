package api.media.kg.dto;

import api.media.kg.validation.EmailValidation;
import api.media.kg.validation.PasswordValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank
    private String name;
    @EmailValidation
    private String username;
    @PasswordValidation
    private String password;
}
