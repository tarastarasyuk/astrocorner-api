package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.SignInRequestDto;
import com.itzroma.astrocornerapi.model.dto.SignUpRequestDto;
import com.itzroma.astrocornerapi.model.entity.User;
import jakarta.transaction.Transactional;

public interface AuthService {

    @Transactional
    User signUp(SignUpRequestDto signUpRequestDto);

    @Transactional
    AuthResponseDto signIn(SignInRequestDto signInRequestDto);

}
