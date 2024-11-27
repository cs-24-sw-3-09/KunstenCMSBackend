package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;

public interface EmailService {
    String sendSimpleMail(EmailDetailsEntity details);
}
