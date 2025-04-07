package api.media.kg.service;

import api.media.kg.dto.CodeConfirmDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.profile.ProfileDetailUpdateDTO;
import api.media.kg.dto.profile.ProfileUpdatePasswordDTO;
import api.media.kg.dto.profile.ProfileUpdateUsernameDTO;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.ProfileRepository;
import api.media.kg.repository.ProfileRoleRepository;
import api.media.kg.util.EmailUtil;
import api.media.kg.util.JwtUtil;
import api.media.kg.util.PhoneUtil;
import api.media.kg.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleService bundleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendingService emailSendingService;
    private final SmsSendService smsSendService;
    private final SmsHistoryService smsHistoryService;
    private final EmailHistoryService emailHistoryService;
    private final ProfileRoleRepository profileRoleRepository;
    private final AttachService attachService;


    public ProfileEntity getProfileId(Long id) {
        log.error("Profile not found id: {}", id);
        return profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public SimpleResponse updateProfile(ProfileDetailUpdateDTO dto, AppLanguage lang) {
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        profileRepository.updateDetail(profileId, dto.getName());
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("profile.update.success", lang));
    }

    public SimpleResponse updatePassword(ProfileUpdatePasswordDTO dto, AppLanguage lang) {
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        ProfileEntity profile = getProfileId(profileId);
        if (!passwordEncoder.matches(dto.getOldPassword(), profile.getPassword())) {
            log.warn("Old password incorrect {}", dto.getOldPassword());
            throw new BadRequestException(bundleService.getMessage("old.password.incorrect", lang));
        }
        profileRepository.updatePassword(profileId, passwordEncoder.encode(dto.getNewPassword()));
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("password.update.successful", lang));
    }
    public SimpleResponse updateUsername(ProfileUpdateUsernameDTO dto, AppLanguage lang) {
//        * check
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if(optional.isPresent() && optional.get() != null){
            log.warn("Email or phone already exists {}", dto.getUsername());
            throw new BadRequestException(bundleService.getMessage("email.phone.exists", lang));
        }
        //        * save
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        profileRepository.updateTempUsername(profileId, dto.getUsername());

        //        * send confirm code
        if (EmailUtil.isEmail(dto.getUsername())) {
            emailSendingService.sendChangeUsernameEmail(dto.getUsername(),lang);
            return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("email.confirm.send", lang));
        } else if (PhoneUtil.isPhone(dto.getUsername())) {
            smsSendService.sendChangeUsernameConfirmSms(dto.getUsername(), lang);
            return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("sms.confirm.send", lang));
        }
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("reset.password.successful", lang));
    }

    public SimpleResponse updateUsernameConfirm(CodeConfirmDTO dto, AppLanguage lang) {
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        ProfileEntity profile = getProfileId(profileId);
        String tempUsername = profile.getTempUsername();
        //  *      check
        if (EmailUtil.isEmail(tempUsername)) {
            emailHistoryService.check(tempUsername, dto.getCode(), lang);
        } else if (PhoneUtil.isPhone(tempUsername)) {
            smsHistoryService.check(tempUsername, dto.getCode(), lang);
        }
        // * update
        profileRepository.updateUsername(profileId, tempUsername);
        // * return jwt
        List<ProfileRole> roles = profileRoleRepository.getAllRolesListByProfile(profile.getId());
        String jwt = JwtUtil.encode(tempUsername, profile.getId(), roles);
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("update.username.confirm", lang), jwt);

    }

    public SimpleResponse updatePhoto(String photoId, AppLanguage lang) {
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        ProfileEntity profile = getProfileId(profileId);

        if (profile.getPhotoId() != null && !profile.getPhotoId().equals(photoId)) {
            log.info("Delete old photo {}", profile.getPhotoId());
            attachService.delete(profile.getPhotoId());
        }
        profileRepository.updatePhoto(profileId, photoId);

        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("photo.update.success", lang));
    }
}
