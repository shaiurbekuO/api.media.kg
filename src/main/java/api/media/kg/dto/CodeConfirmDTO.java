package api.media.kg.dto;

import jakarta.validation.constraints.NotBlank;

public class CodeConfirmDTO {
    @NotBlank(message = "Code is required")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
