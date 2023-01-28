package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.exception.EntityExistsException;
import com.itzroma.astrocornerapi.exception.EntityNotFoundException;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("Email is already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            throw new EntityNotFoundException("User not found");
        });
    }
}
