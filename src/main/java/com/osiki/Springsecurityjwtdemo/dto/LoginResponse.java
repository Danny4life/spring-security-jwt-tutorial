package com.osiki.Springsecurityjwtdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String responseCode;
    private String responseMessage;
    private LoginInfo loginInfo;
}
