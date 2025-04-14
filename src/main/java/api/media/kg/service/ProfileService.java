package api.media.kg.service;

import api.media.kg.dto.CodeConfirmDTO;
import api.media.kg.dto.ProfileDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.profile.ProfileDetailUpdateDTO;
import api.media.kg.dto.profile.ProfileFilterDTO;
import api.media.kg.dto.profile.ProfileUpdatePasswordDTO;
import api.media.kg.dto.profile.ProfileUpdateUsernameDTO;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.entity.ProfileRoleEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.mapper.ProfileDetailMapper;
import api.media.kg.repository.ProfileRepository;
import api.media.kg.repository.ProfileRoleRepository;
import api.media.kg.util.EmailUtil;
import api.media.kg.util.JwtUtil;
import api.media.kg.util.PhoneUtil;
import api.media.kg.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<ProfileDTO> profileFilter(ProfileFilterDTO dto, int page, int size, AppLanguage lang) {
        PageRequest pageRequest = PageRequest.of(page, size);  // туура PageRequest түзүлүп жатканын текшерүү
        Page<ProfileDetailMapper> filterResult = null;
        if(dto.getQuery() == null) {
            filterResult = profileRepository.filter(pageRequest);
        } else {
            filterResult = profileRepository.filter(dto.getQuery(), pageRequest);
        }

        List<ProfileDTO> list = filterResult.stream().map(this::toDTO).toList();
        return new PageImpl<>(list, pageRequest, filterResult.getTotalElements());
    }

    public SimpleResponse changeStatus(Long id, GeneralStatus status, AppLanguage lang) {
        profileRepository.changeStatus(id, status);
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("status.update.successful", lang));
    }
    public SimpleResponse delete(Long id, AppLanguage lang) {
        profileRepository.delete(id);
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("profile.delete.successful", lang));
    }
    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUsername(entity.getUsername());
        if(entity.getRoleList() != null){
            List<ProfileRole> roleList = entity.getRoleList().stream().map(ProfileRoleEntity::getRoles).toList();
            dto.setRoleList(roleList);
        }
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setStatus(entity.getStatus());
        dto.setPhoto(attachService.attachDTO(entity.getPhotoId()));
        return dto;
    }
    public ProfileDTO toDTO(ProfileDetailMapper mapper) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(mapper.getId());
        dto.setName(mapper.getName());
        dto.setUsername(mapper.getUsername());
        if(mapper.getRoles() != null){
            List<ProfileRole> roleList = Arrays.stream(mapper.getRoles().split(",")).map(ProfileRole::valueOf).toList();
            dto.setRoleList(roleList);
        }
        dto.setCreatedDate(mapper.getCreatedDate());
        dto.setStatus(mapper.getStatus());
        dto.setPostCount(mapper.getPostCount());
        dto.setPhoto(attachService.attachDTO(mapper.getPhotoId()));
        return dto;
    }



}
