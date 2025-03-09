package api.media.kg.service;

import api.media.kg.dto.*;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.ProfileRepository;
import api.media.kg.repository.ProfileRoleRepository;
import api.media.kg.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRoleService profileRoleService;
    private final EmailSendingService emailSendingService;
    private final ProfileService profileService;
    private final AuthenticationManager authenticationManager;
//?   private final JwtTokenProvider jwtTokenProvider;

    public AuthService(ProfileRepository profileRepository, ProfileRoleRepository profileRoleRepository,
                       PasswordEncoder passwordEncoder,
                       ProfileRoleService profileRoleService, EmailSendingService emailSendingService, ProfileService profileService,
                       AuthenticationManager authenticationManager){
        this.profileRepository = profileRepository;
        this.profileRoleRepository = profileRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRoleService = profileRoleService;
        this.emailSendingService = emailSendingService;
        this.profileService = profileService;
        this.authenticationManager = authenticationManager;
    }


    public SimpleResponse registration(RegistrationDTO registrationDTO) {
        Optional<ProfileEntity> profile = profileRepository.findByUsernameAndVisibleTrue(registrationDTO.getUsername());

        if (profile.isPresent()) {
            ProfileEntity profileEntity = profile.get();
            if(profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRoleService.deleteProfileRole(profileEntity.getId());
                profileRepository.delete(profileEntity);
            }else {
                throw new BadRequestException("Email already exists");
            }
        }
        ProfileEntity newProfile = new ProfileEntity();
        newProfile.setName(registrationDTO.getName());
        newProfile.setUsername(registrationDTO.getUsername());
        newProfile.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newProfile.setStatus(GeneralStatus.ACTIVE); 
        newProfile.setCreatedDate(java.time.LocalDate.now());
        newProfile.setVisible(true);
        profileRepository.save(newProfile);
//*      insert role
        profileRoleService.createProfileRole(newProfile.getId(), ProfileRole.ROLE_USER);
//?        emailSendingService.sendRegistrationEmail(registrationDTO.getUsername(), newProfile.getId());
        return new SimpleResponse(HttpStatus.OK, "Registration successful");
    }

//*    reg validation
    public SimpleResponse regValidation(String token) {
       Long profileId = JwtUtil.decodeRegVerToken(token);
        ProfileEntity profile = profileService.getProfileId(profileId);
        try {
           if(profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
//*            Active
               profileRepository.changeStatus(profileId, GeneralStatus.ACTIVE);
           }else {
               throw new BadRequestException("Verification failed");}
       }catch (JwtException e) {
           throw new BadRequestException("Verification failed");}
        return new SimpleResponse(HttpStatus.OK, "Registration successful");
    }

    public ProfileDTO login(LoginDTO loginDTO) {
        Optional<ProfileEntity> profile = profileRepository.findByUsernameAndVisibleTrue(loginDTO.getUsername());
        if(profile.isEmpty()){
            throw new BadRequestException("Username or password is incorrect");
        }
        ProfileEntity profileEntity = profile.get();
        if(!passwordEncoder.matches(loginDTO.getPassword(), profileEntity.getPassword())){
            throw new BadRequestException("Username or password is incorrect");
        }
        if(!profileEntity.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new BadRequestException("Account is not active");
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(profileEntity.getName());
        profileDTO.setUsername(profileEntity.getUsername());
        profileDTO.setRoleList(profileRoleRepository.getAllRolesListByProfile(profileEntity.getId()));
        profileDTO.setJwt(JwtUtil.encode(profileEntity.getId(), profileDTO.getRoleList()));
        return profileDTO;

    }
}
