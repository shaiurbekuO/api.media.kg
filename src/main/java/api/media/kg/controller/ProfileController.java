package api.media.kg.controller;

import api.media.kg.dto.CodeConfirmDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.profile.ProfileDetailUpdateDTO;
import api.media.kg.dto.profile.ProfilePhotoUpdateDTO;
import api.media.kg.dto.profile.ProfileUpdatePasswordDTO;
import api.media.kg.dto.profile.ProfileUpdateUsernameDTO;
import api.media.kg.enums.AppLanguage;
import api.media.kg.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PutMapping("/detail")
    public SimpleResponse updateProfile(@Valid @RequestBody ProfileDetailUpdateDTO dto,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updateProfile(dto, lang);
    }
    @PutMapping("/photo")
    public SimpleResponse updatePhoto(@Valid @RequestBody ProfilePhotoUpdateDTO dto,
                                        @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updatePhoto(dto.getPhotoId(), lang);
    }
    @PutMapping("/password")
    public SimpleResponse updatePassword(@Valid @RequestBody ProfileUpdatePasswordDTO dto,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updatePassword(dto, lang);
    }
    @PutMapping("/username")
    public SimpleResponse updateUsername(@Valid @RequestBody ProfileUpdateUsernameDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updateUsername(dto, lang);
    }

    @PutMapping("/username/confirm")
    public SimpleResponse updateUsernameConfirm(@Valid @RequestBody CodeConfirmDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updateUsernameConfirm(dto, lang);
    }
}
