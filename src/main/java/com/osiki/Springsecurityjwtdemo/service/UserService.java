package com.osiki.Springsecurityjwtdemo.service;

import com.osiki.Springsecurityjwtdemo.dto.AuthResponse;
import com.osiki.Springsecurityjwtdemo.dto.RegisterRequestDto;

public interface UserService {
    AuthResponse register(RegisterRequestDto registerRequestDto);
}
