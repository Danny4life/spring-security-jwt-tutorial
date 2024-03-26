package com.osiki.Springsecurityjwtdemo.service.impl;

import com.osiki.Springsecurityjwtdemo.config.JwtService;
import com.osiki.Springsecurityjwtdemo.dto.*;
import com.osiki.Springsecurityjwtdemo.entity.UserEntity;
import com.osiki.Springsecurityjwtdemo.enums.Role;
import com.osiki.Springsecurityjwtdemo.repository.UserRepository;
import com.osiki.Springsecurityjwtdemo.service.EmailSenderService;
import com.osiki.Springsecurityjwtdemo.service.UserService;
import com.osiki.Springsecurityjwtdemo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailService;;

    @Override
    public AuthResponse register(RegisterRequestDto registerRequestDto) {
        UserEntity user = UserEntity.builder()
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(Role.USER)
                .build();

        UserEntity saveUser = userRepository.save(user);

        // send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has Been Successfully Created.")
                .build();
        emailService.sendEmailAlert(emailDetails);


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

    @Override
    public LoginResponse login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE)
                .loginInfo(LoginInfo.builder()
                        .email(user.getEmail())
                        .token(jwtToken)
                        .build())
                //.token(jwtToken)
                .build();
    }
}
