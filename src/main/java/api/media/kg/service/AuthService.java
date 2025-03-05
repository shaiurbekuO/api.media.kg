package api.media.kg.service;

import api.media.kg.dto.RegistrationDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRoleService profileRoleService;

    public AuthService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder, ProfileRoleService profileRoleService) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRoleService = profileRoleService;
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
        newProfile.setStatus(GeneralStatus.IN_REGISTRATION);
        newProfile.setCreatedDate(java.time.LocalDate.now());
        newProfile.setVisible(true);
        profileRepository.save(newProfile);
//*      insert role
        profileRoleService.createProfileRole(newProfile.getId(), ProfileRole.ROLE_USER);

        return new SimpleResponse(HttpStatus.OK, "Registration successful");
    }

}
