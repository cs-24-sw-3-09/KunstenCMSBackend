package com.github.cs_24_sw_3_09.CMS.services;

import java.sql.Time;

import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;

public interface EmailService {
    String sendSimpleMail(EmailDetailsEntity details);

    String sendDDDisconnectMail(int id);

    String sendDDDisconnectMail(int id, Time currentTime);
}
