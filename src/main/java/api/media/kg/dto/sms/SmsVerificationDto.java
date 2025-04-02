package api.media.kg.dto.sms;

import api.media.kg.validation.PhoneNumberValidation;
import jakarta.validation.constraints.NotBlank;

public class SmsVerificationDto {
    @PhoneNumberValidation
    private String phone;
    @NotBlank(message = "Code is required")
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
