package com.itzroma.astrocornerapi.security.userdetails;

import com.itzroma.astrocornerapi.model.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtUserDetails extends org.springframework.security.core.userdetails.User {
    public JwtUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public JwtUserDetails(String username,
                          String password,
                          boolean enabled,
                          boolean accountNonExpired,
                          boolean credentialsNonExpired,
                          boolean accountNonLocked,
                          Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static JwtUserDetails fromUser(User user) {
        return new JwtUserDetails(
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
