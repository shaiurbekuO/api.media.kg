package api.media.kg.service;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileEntity getProfileId(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}
