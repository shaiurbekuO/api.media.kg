package api.media.kg.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDTO {
    @NotBlank(message = "Username is required")
    private String username;

    public @NotBlank(message = "Username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required") String username) {
        this.username = username;
    }
}
