package api.media.kg.service;

import api.media.kg.dto.SimpleResponse;
import api.media.kg.entity.ProfileRoleEntity;
import api.media.kg.enums.ProfileRole;
import api.media.kg.repository.ProfileRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProfileRoleService {
    private final ProfileRoleRepository profileRoleRepository;

    public ProfileRoleService(ProfileRoleRepository profileRoleRepository) {
        this.profileRoleRepository = profileRoleRepository;
    }

    public SimpleResponse createProfileRole(Long profileId, ProfileRole role) {
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(profileId);
        entity.setRoles(role);
        entity.setCreatedDate(java.time.LocalDate.now());
        profileRoleRepository.save(entity);
        return new SimpleResponse(HttpStatus.OK,"Profile role success");


    }

    public void deleteProfileRole(Long profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
}
