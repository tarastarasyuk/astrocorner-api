package com.itzroma.astrocornerapi.security.userdetails;

import com.itzroma.astrocornerapi.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultUserDetails implements OAuth2User, UserDetails {
    private Long id;
    private String email;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public DefaultUserDetails(Long id, String email, String password, boolean enabled ,Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static DefaultUserDetails fromUser(User user) {
        return new DefaultUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                Collections.singleton(user.getRole())
        );
    }

    public static DefaultUserDetails create(User user) {
        List<GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("USER"));

        return new DefaultUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                authorities
        );
    }

    public static DefaultUserDetails create(User user, Map<String, Object> attributes) {
        DefaultUserDetails userPrincipal = DefaultUserDetails.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
