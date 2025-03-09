package api.media.kg.config.security;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.entity.ProfileRoleEntity;
import api.media.kg.enums.ProfileRole;
import api.media.kg.repository.ProfileRepository;
import api.media.kg.repository.ProfileRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;

    public UserDetailsServiceImpl(ProfileRepository profileRepository, ProfileRoleRepository profileRoleRepository) {
        this.profileRepository = profileRepository;
        this.profileRoleRepository = profileRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        List<ProfileRole> roles = profileRoleRepository.getAllRolesListByProfile(profile.getId());
        
        return new UserDetailsImpl(profile, roles);
    }
}
