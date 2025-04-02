package api.media.kg.dto.sms;

import api.media.kg.validation.PhoneNumberValidation;

public class SmsResendDto {
    @PhoneNumberValidation
    private String phone;
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
