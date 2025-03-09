package api.media.kg.config.security;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.entity.ProfileRoleEntity;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.repository.ProfileRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean isActive;

    public UserDetailsImpl(ProfileEntity profile, List<ProfileRoleEntity> roles) {
        this.username = profile.getUsername();
        this.password = profile.getPassword();
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoles().name()))
                .collect(Collectors.toList());
        this.isActive = profile.getStatus() == GeneralStatus.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
