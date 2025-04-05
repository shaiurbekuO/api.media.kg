package api.media.kg.dto.profile;

import api.media.kg.validation.PasswordValidation;

public class ProfileUpdatePasswordDTO {
    @PasswordValidation
    private String oldPassword;
    @PasswordValidation
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
