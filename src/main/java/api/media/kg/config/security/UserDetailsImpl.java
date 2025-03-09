package api.media.kg.config.security;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.entity.ProfileRoleEntity;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.repository.ProfileRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private GeneralStatus status;

    public UserDetailsImpl(ProfileEntity profile,
                           List<ProfileRole> roleList) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.username = profile.getUsername();
        this.password = profile.getPassword();
        this.status = profile.getStatus();
        this.authorities = roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role.name())).toList();

    }
//    public UserDetailsImpl(ProfileEntity profile, List<ProfileRoleEntity> roles) {
//        this.username = profile.getUsername();
//        this.password = profile.getPassword();
//        this.authorities = roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getRoles().name()))
//                .collect(Collectors.toList());
//        this.isActive = profile.getStatus() == GeneralStatus.ACTIVE;
//    }

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
        return status.equals(GeneralStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
