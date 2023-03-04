package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.RegisterRequestDto;
import com.itzroma.astrocornerapi.model.dto.SignInRequestDto;
import com.itzroma.astrocornerapi.model.entity.User;
import jakarta.transaction.Transactional;

public interface AuthService {

    @Transactional
    User standardSignUp(RegisterRequestDto registerRequestDto);

    @Transactional
    AuthResponseDto signIn(SignInRequestDto signInRequestDto);

}
