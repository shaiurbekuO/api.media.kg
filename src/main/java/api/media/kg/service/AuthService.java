package api.media.kg.service;

import api.media.kg.dto.RegistrationDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.entity.ProfileEntity;
import api.media.kg.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;


    public SimpleResponse Registration(RegistrationDTO registrationDTO) {
        Optional<ProfileEntity> profile = profileRepository.findByUsernameAndVisibleTrue(registrationDTO.getUsername());
        if(profile.isPresent()) {
            return new SimpleResponse(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        return null;

    }
}
