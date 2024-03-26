package com.osiki.Springsecurityjwtdemo.service;

import com.osiki.Springsecurityjwtdemo.dto.EmailDetails;

public interface EmailSenderService {
    void sendEmailAlert(EmailDetails emailDetails);
}
