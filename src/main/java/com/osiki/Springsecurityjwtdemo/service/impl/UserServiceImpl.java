package com.osiki.Springsecurityjwtdemo.service.impl;

import com.osiki.Springsecurityjwtdemo.config.JwtService;
import com.osiki.Springsecurityjwtdemo.dto.AuthResponse;
import com.osiki.Springsecurityjwtdemo.dto.RegisterRequestDto;
import com.osiki.Springsecurityjwtdemo.dto.RegistrationInfo;
import com.osiki.Springsecurityjwtdemo.entity.UserEntity;
import com.osiki.Springsecurityjwtdemo.enums.Role;
import com.osiki.Springsecurityjwtdemo.repository.UserRepository;
import com.osiki.Springsecurityjwtdemo.service.UserService;
import com.osiki.Springsecurityjwtdemo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequestDto registerRequestDto) {
        UserEntity user = UserEntity.builder()
                .firstName(registerRequestDto.getFirstname())
                .lastName(registerRequestDto.getLastname())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .registrationInfo(RegistrationInfo.builder()

                        .firstname(user.getFirstName())
                        .lastname(user.getLastName())
                        .email(user.getEmail())
                        .token(jwtToken)
                        .build())

                //.token(jwtToken)
                .build();
    }
}
