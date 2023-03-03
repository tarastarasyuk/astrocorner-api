package com.itzroma.astrocornerapi.service.impl;

import com.itzroma.astrocornerapi.exception.BadCredentialsException;
import com.itzroma.astrocornerapi.exception.EmailTakenException;
import com.itzroma.astrocornerapi.model.entity.AuthProvider;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.UserRepository;
import com.itzroma.astrocornerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailTakenException();
        }

        // TODO: Change flow
        user.setProvider(AuthProvider.GOOGLE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(BadCredentialsException::new);
    }

    @Override
    public void enableUser(User user) {
        userRepository.enableUser(user.getEmail());
    }
}
