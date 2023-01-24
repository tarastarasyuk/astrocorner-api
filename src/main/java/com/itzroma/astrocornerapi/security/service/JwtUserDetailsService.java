package com.itzroma.astrocornerapi.security.service;

import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.UserRepository;
import com.itzroma.astrocornerapi.security.userdetails.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("Cannot load user by username (email): username (email) not found");
        });
        return JwtUserDetails.fromUser(user);
    }
}
