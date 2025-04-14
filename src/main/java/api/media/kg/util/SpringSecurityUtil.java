package api.media.kg.util;

import api.media.kg.config.security.UserDetailsImpl;
import api.media.kg.enums.ProfileRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

public class SpringSecurityUtil {

    public static UserDetailsImpl getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return user;
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return user.getId();
    }
    public static boolean hasRole(ProfileRole requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals(requiredRole.name()));
    }
}
