package api.media.kg.controller;

import api.media.kg.dto.CodeConfirmDTO;
import api.media.kg.dto.ProfileDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.profile.*;
import api.media.kg.enums.AppLanguage;
import api.media.kg.service.ProfileService;
import api.media.kg.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Tag(name = "Profile Management", description = "API for managing user profile information")
@Slf4j
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

    @Operation(summary = "Profile filter",
            description = "Api used for profile filter")
    @PostMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ProfileDTO>> profileFilter(@RequestBody ProfileFilterDTO dto,
                                                         @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang,
                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(profileService.profileFilter(dto, PageUtil.getPage(page), size, lang));
    }

    @Operation(summary = "Profile status",
            description = "Api used for Profile check status")
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SimpleResponse> status(@PathVariable("id") Long id,
                                                 @Valid @RequestBody ProfileStatusDTO dto,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang){
        return ResponseEntity.ok(profileService.changeStatus(id,dto.getStatus(), lang));
    }

    @Operation(summary = "Profile delete",
            description = "Api used for Profile delete")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SimpleResponse> delete(@PathVariable("id") Long id,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang){
        return ResponseEntity.ok(profileService.delete(id, lang));
    }


    @Operation(summary = "Activate Profile post",
            description = "Admin can activate another user's notActive profile post")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SimpleResponse> statusPost(
            @PathVariable("id") String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return ResponseEntity.ok(profileService.statusPost(id, lang));
    }




}
