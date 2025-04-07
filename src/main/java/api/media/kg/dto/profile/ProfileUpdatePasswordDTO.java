package api.media.kg.dto.profile;

import api.media.kg.validation.PasswordValidation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdatePasswordDTO {
    @PasswordValidation
    private String oldPassword;
    @PasswordValidation
    private String newPassword;


}
