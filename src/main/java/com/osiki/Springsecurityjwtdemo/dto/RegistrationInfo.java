package com.osiki.Springsecurityjwtdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationInfo {
    private String firstname;
    private String lastname;
    private String email;
    private String token;

}
