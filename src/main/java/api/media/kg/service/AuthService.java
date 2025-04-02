package api.media.kg.service;

import api.media.kg.dto.*;
import api.media.kg.dto.auth.RegistrationDTO;
import api.media.kg.dto.auth.ResetPasswordConfirmDTO;
import api.media.kg.dto.auth.ResetPasswordDTO;
import api.media.kg.dto.sms.SmsResendDto;
import api.media.kg.dto.sms.SmsVerificationDto;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.ProfileRepository;
import api.media.kg.repository.ProfileRoleRepository;
import api.media.kg.util.EmailUtil;
import api.media.kg.util.JwtUtil;
import api.media.kg.util.PhoneUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRoleService profileRoleService;
    private final ProfileService profileService;
    private final ResourceBundleService bundleService;
    private final SmsSendService smsSendService;
    private final SmsHistoryService smsHistoryService;
    private final EmailSendingService emailSendingService;
    private final EmailHistoryService emailHistoryService;

    public AuthService(ProfileRepository profileRepository, ProfileRoleRepository profileRoleRepository,
                       PasswordEncoder passwordEncoder,
                       ProfileRoleService profileRoleService, ProfileService profileService, ResourceBundleService resourceBundleService, SmsSendService smsSendService,
                       SmsHistoryService smsHistoryService, EmailSendingService emailSendingService, EmailHistoryService emailHistoryService){
        this.profileRepository = profileRepository;
        this.profileRoleRepository = profileRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRoleService = profileRoleService;
        this.profileService = profileService;
        this.bundleService = resourceBundleService;
        this.smsSendService = smsSendService;
        this.smsHistoryService = smsHistoryService;
        this.emailSendingService = emailSendingService;
        this.emailHistoryService = emailHistoryService;
    }


    public SimpleResponse registration(RegistrationDTO registrationDTO, AppLanguage lang) {
        Optional<ProfileEntity> profile = profileRepository.findByUsernameAndVisibleTrue(registrationDTO.getUsername());

        if (profile.isPresent()) {
            ProfileEntity profileEntity = profile.get();
            if (profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRoleService.deleteProfileRole(profileEntity.getId());
                profileRepository.delete(profileEntity);
            } else {
                throw new BadRequestException(bundleService.getMessage("email.phone.exists", lang));
            }
        }

        ProfileEntity newProfile = new ProfileEntity();
        newProfile.setName(registrationDTO.getName());
        newProfile.setUsername(registrationDTO.getUsername());
        newProfile.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newProfile.setStatus(GeneralStatus.IN_REGISTRATION);
        newProfile.setCreatedDate(java.time.LocalDate.now());
        newProfile.setVisible(true);
        profileRepository.save(newProfile);

        // Insert role
        profileRoleService.createProfileRole(newProfile.getId(), ProfileRole.ROLE_USER);

        // Send email or SMS based on username type
        if (EmailUtil.isEmail(registrationDTO.getUsername())) {
            emailSendingService.sendRegistrationEmail(registrationDTO.getUsername(), newProfile.getId(), lang);
            return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("email.confirm.send", lang));
        } else if (PhoneUtil.isPhone(registrationDTO.getUsername())) {
            smsSendService.sendRegistrationSms(registrationDTO.getUsername());
            return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("sms.confirm.send", lang));
        }

        return new SimpleResponse(HttpStatus.BAD_REQUEST, bundleService.getMessage("verification.failed", lang));
    }

    public SimpleResponse registrationEmailVerification(String token, AppLanguage lang) {
       Long profileId = JwtUtil.decodeRegVerToken(token);
        ProfileEntity profile = profileService.getProfileId(profileId);
//?        базага сактап анан ошол тилди берип жиберуу
//?        AppLanguage lang = profile.getLang();
        try {
           if(profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
//*            Active
               profileRepository.changeStatus(profileId, GeneralStatus.ACTIVE);
           }else {
               throw new BadRequestException(bundleService.getMessage("verification.failed", lang));
           }
       }catch (JwtException e) {
           throw new BadRequestException(bundleService.getMessage("verification.failed", lang));}
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("registration.successful", lang));
    }

    public ProfileDTO login(LoginDTO loginDTO, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(loginDTO.getUsername());
        if(optional.isEmpty()){
            throw new BadRequestException(bundleService.getMessage("username.or.password.is.incorrect", lang));
        }
        ProfileEntity profile = optional.get();
        if(!passwordEncoder.matches(loginDTO.getPassword(), profile.getPassword())){
            throw new BadRequestException(bundleService.getMessage("username.or.password.is.incorrect", lang));
        }
        if(!profile.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new BadRequestException(bundleService.getMessage("account.is.not.active", lang));
        }
        return getLogInResponse(profile);

    }

    public ProfileDTO registrationSmsVerification(SmsVerificationDto dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getPhone());
        if(optional.isEmpty()) {
            throw new BadRequestException(bundleService.getMessage("verification.failed",  lang));
        }
        ProfileEntity profile = optional.get();
        if(!profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new BadRequestException(bundleService.getMessage("verification.failed",  lang));
        }
//        code check
       smsHistoryService.check(dto.getPhone(), dto.getCode(), lang);
//        active
        profileRepository.changeStatus(profile.getId(), GeneralStatus.ACTIVE);
//        response
        return getLogInResponse(profile);
    }
    public SimpleResponse registrationSmsVerificationResend(SmsResendDto dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getPhone());
        if(optional.isEmpty()) {
            throw new BadRequestException(bundleService.getMessage("verification.failed",  lang));
        }
        ProfileEntity profile = optional.get();
        if(!profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new BadRequestException(bundleService.getMessage("verification.failed",  lang));
        }
    //*      send sms
        smsSendService.sendRegistrationSms(dto.getPhone());
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("sms.confirm.send", lang));
    }
    public SimpleResponse resetPassword(ResetPasswordDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()) {
            throw new BadRequestException(bundleService.getMessage("profile.not.found", lang));
        }
        ProfileEntity profile = optional.get();
        if(!profile.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new BadRequestException(bundleService.getMessage("account.is.not.active", lang));
        }
        // Send email or SMS based on username type
        if (EmailUtil.isEmail(dto.getUsername())) {
            emailSendingService.sendResetPasswordEmail(dto.getUsername(), lang);
        } else if (PhoneUtil.isPhone(dto.getUsername())) {
            smsSendService.sendResetPasswordSms(dto.getUsername());
        }
        String response = bundleService.getMessage("reset.password.successful", lang);
        return new SimpleResponse(HttpStatus.OK, String.format(response, dto.getUsername()));
    }
    public SimpleResponse resetPasswordConfirm(ResetPasswordConfirmDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()) {
            throw new BadRequestException(bundleService.getMessage("profile.not.found", lang));
        }
        ProfileEntity profile = optional.get();
        if(!profile.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new BadRequestException(bundleService.getMessage("account.is.not.active", lang));
        }
//  *      check
        if (EmailUtil.isEmail(dto.getUsername())) {
            emailHistoryService.check(dto.getUsername(), dto.getConfirmCode(), lang);
        } else if (PhoneUtil.isPhone(dto.getUsername())) {
            smsSendService.sendResetPasswordSms(dto.getUsername());
        }
//        * update
        profileRepository.updatePassword(profile.getId(), passwordEncoder.encode(dto.getPassword()));
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("password.successful", lang));
    }
    public ProfileDTO getLogInResponse(ProfileEntity profile) {
        ProfileDTO response = new ProfileDTO();
        response.setName(profile.getName());
        response.setUsername(profile.getUsername());
        response.setRoleList(profileRoleRepository.getAllRolesListByProfile(profile.getId()));
        response.setJwt(JwtUtil.encode(profile.getUsername(), profile.getId(), response.getRoleList()));
        return response;
    }

}
