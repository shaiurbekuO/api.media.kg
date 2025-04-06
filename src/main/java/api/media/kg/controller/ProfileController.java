package api.media.kg.controller;

import api.media.kg.dto.CodeConfirmDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.profile.ProfileDetailUpdateDTO;
import api.media.kg.dto.profile.ProfilePhotoUpdateDTO;
import api.media.kg.dto.profile.ProfileUpdatePasswordDTO;
import api.media.kg.dto.profile.ProfileUpdateUsernameDTO;
import api.media.kg.enums.AppLanguage;
import api.media.kg.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Tag(name = "Profile Management", description = "API for managing user profile information")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @Operation(summary = "Update profile details",
            description = "Updates basic user profile information (name, email, phone, etc.)")
    @PutMapping("/detail")
    public SimpleResponse updateProfile(@Valid @RequestBody ProfileDetailUpdateDTO dto,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updateProfile(dto, lang);
    }
    @Operation(summary = "Update profile photo",
            description = "Changes the user's profile picture using photo ID")
    @PutMapping("/photo")
    public SimpleResponse updatePhoto(@Valid @RequestBody ProfilePhotoUpdateDTO dto,
                                        @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updatePhoto(dto.getPhotoId(), lang);
    }
    @Operation(summary = "Update password",
            description = "Changes the user's account password (requires current password verification)")
    @PutMapping("/password")
    public SimpleResponse updatePassword(@Valid @RequestBody ProfileUpdatePasswordDTO dto,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updatePassword(dto, lang);
    }
    @Operation(summary = "Request username change",
            description = "Initiates username change process (may send verification code)")
    @PutMapping("/username")
    public SimpleResponse updateUsername(@Valid @RequestBody ProfileUpdateUsernameDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updateUsername(dto, lang);
    }


    @Operation(summary = "Confirm username change",
            description = "Confirms username change using verification code")
    @PutMapping("/username/confirm")
    public SimpleResponse updateUsernameConfirm(@Valid @RequestBody CodeConfirmDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return profileService.updateUsernameConfirm(dto, lang);
    }
}
