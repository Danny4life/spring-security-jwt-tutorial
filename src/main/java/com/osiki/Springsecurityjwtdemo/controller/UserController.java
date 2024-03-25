package com.osiki.Springsecurityjwtdemo.controller;

import com.osiki.Springsecurityjwtdemo.dto.AuthResponse;
import com.osiki.Springsecurityjwtdemo.dto.RegisterRequestDto;
import com.osiki.Springsecurityjwtdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")

    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDto registerRequestDto){

        return ResponseEntity.ok(userService.register(registerRequestDto));

    }
}
