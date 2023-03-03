package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    User save(User user);

    User findByEmail(String email);

    void enableUser(User user);

}
