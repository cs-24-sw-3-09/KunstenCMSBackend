package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.UserRepository;
import com.github.cs_24_sw_3_09.CMS.services.EmailService;

import lombok.Setter;

@Setter
@Service
public class MailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private DisplayDeviceRepository displayDeviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${EMAIL.USERNAME:}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailDetailsEntity details) {
        // Try block to check for exceptions
        System.out.println(sender);
        System.out.println(details.toString());
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail...";
        }
    }

    @Override
    public String sendDDDisconnectMail(int id, String receiver) {
        if (!displayDeviceRepository.existsById(id)) {
            return "Did not sent to DD, as it is not found in DB";
        }

        EmailDetailsEntity email = EmailDetailsEntity.builder()
                .msgBody("<b>Screen with ID</b> " + id + " have disconnected")
                .subject("Disconnected Screen: " + id)
                .build();
        //String mailResult = sendSimpleMail(email);

        Set<UserEntity> userList = userRepository.findUserWithNotificationsEnabled();
        userList = filterUsersOutsidePausePeriod(userList); // filters user outside of perioed
        for (UserEntity user : userList) {
            email.setRecipient(user.getEmail());
            String mailResult = sendSimpleMail(email);
        }
        System.out.println(userList.toString());
        return "Worked";
    }//

    public static Set<UserEntity> filterUsersOutsidePausePeriod(Set<UserEntity> users) {
        Date currentTime = new Date();

        return users.stream()
                .filter(user -> isOutsidePausePeriod(user, currentTime))
                .collect(Collectors.toSet());
    }

    private static boolean isOutsidePausePeriod(UserEntity user, Date currentTime) {
        Date start = user.getPauseNotificationStart();
        Date end = user.getPauseNotificationEnd();

        // If either start or end is null, consider it outside the pause period
        if (start == null || end == null) {
            return true;
        }

        // Check if the current time is outside the pause period
        return currentTime.before(start) || currentTime.after(end);
    }

}
