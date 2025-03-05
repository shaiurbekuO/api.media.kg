package api.media.kg.dto;

import api.media.kg.validation.EmailValidation;
import api.media.kg.validation.PasswordValidation;
import jakarta.validation.constraints.NotBlank;


public class RegistrationDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @EmailValidation
    private String username;
    @PasswordValidation
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
