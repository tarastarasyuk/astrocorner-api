package com.itzroma.astrocornerapi.security.userdetails;

import com.itzroma.astrocornerapi.model.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class DefaultUserDetails extends org.springframework.security.core.userdetails.User {
    public DefaultUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public DefaultUserDetails(String username,
                              String password,
                              boolean enabled,
                              boolean accountNonExpired,
                              boolean credentialsNonExpired,
                              boolean accountNonLocked,
                              Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static DefaultUserDetails fromUser(User user) {
        return new DefaultUserDetails(
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                !user.getLocked(),
                Collections.singleton(user.getRole())
        );
    }
}
