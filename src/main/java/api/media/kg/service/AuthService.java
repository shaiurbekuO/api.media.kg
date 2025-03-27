package api.media.kg.service;

import api.media.kg.dto.*;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.ProfileRepository;
import api.media.kg.repository.ProfileRoleRepository;
import api.media.kg.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {
    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRoleService profileRoleService;
    private final EmailSendingService emailSendingService;
    private final ProfileService profileService;
    private final ResourceBundleService bundleService;
    private final SmsSendService smsSendService;

    public AuthService(ProfileRepository profileRepository, ProfileRoleRepository profileRoleRepository,
                       PasswordEncoder passwordEncoder,
                       ProfileRoleService profileRoleService, EmailSendingService emailSendingService, ProfileService profileService, ResourceBundleService resourceBundleService, SmsSendService smsSendService
    ){
        this.profileRepository = profileRepository;
        this.profileRoleRepository = profileRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRoleService = profileRoleService;
        this.emailSendingService = emailSendingService;
        this.profileService = profileService;
        this.bundleService = resourceBundleService;
        this.smsSendService = smsSendService;
    }


    public SimpleResponse registration(RegistrationDTO registrationDTO, AppLanguage lang) {
        Optional<ProfileEntity> profile = profileRepository.findByUsernameAndVisibleTrue(registrationDTO.getUsername());

        if (profile.isPresent()) {
            ProfileEntity profileEntity = profile.get();
            if(profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRoleService.deleteProfileRole(profileEntity.getId());
                profileRepository.delete(profileEntity);
            }else {
                throw new BadRequestException(bundleService.getMessage("email.phone.exists",  lang));
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
//*      insert role
        profileRoleService.createProfileRole(newProfile.getId(), ProfileRole.ROLE_USER);
//*      send email
//        emailSendingService.sendRegistrationEmail(registrationDTO.getUsername(), newProfile.getId(), lang);
//*      send sms
        smsSendService.SendRegistrationSms(registrationDTO.getUsername());
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("email.confirm.send", lang));
    }

//*    reg email validation
    public SimpleResponse registrationEmailValidation(String token, AppLanguage lang) {
       Long profileId = JwtUtil.decodeRegVerToken(token);
        ProfileEntity profile = profileService.getProfileId(profileId);
//*        базага сактап анан ошол тилди берип жиберуу
//*        AppLanguage lang = profile.getLang();
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
        Optional<ProfileEntity> profile = profileRepository.findByUsernameAndVisibleTrue(loginDTO.getUsername());
        if(profile.isEmpty()){
            throw new BadRequestException(bundleService.getMessage("username.or.password.is.incorrect", lang));
        }
        ProfileEntity profileEntity = profile.get();
        if(!passwordEncoder.matches(loginDTO.getPassword(), profileEntity.getPassword())){
            throw new BadRequestException(bundleService.getMessage("username.or.password.is.incorrect", lang));
        }
        if(!profileEntity.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new BadRequestException(bundleService.getMessage("account.is.not.active", lang));
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(profileEntity.getName());
        profileDTO.setUsername(profileEntity.getUsername());
        profileDTO.setRoleList(profileRoleRepository.getAllRolesListByProfile(profileEntity.getId()));
        profileDTO.setJwt(JwtUtil.encode(profileEntity.getUsername(), profileEntity.getId(), profileDTO.getRoleList()));
        return profileDTO;

    }
}
