package api.media.kg.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordConfirmDTO {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Code is required")
    private String confirmCode;
    @NotBlank(message = "Password is required")
    private String password;

    public @NotBlank(message = "Username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Code is required") String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(@NotBlank(message = "Code is required") String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }
}
