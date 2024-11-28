package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.UserRepository;
import com.github.cs_24_sw_3_09.CMS.services.EmailService;

import jakarta.mail.internet.MimeMessage;
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
            // Create a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Use MimeMessageHelper to set up the message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(details.getMsgBody(), true); // Enable HTML content

            // Send the email
            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully To " + details.getRecipient();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail..." + e.getMessage();
        }

    }


    @Override
    public String sendDDDisconnectMail(int id) {
        // Checks that the ID is in the database. In order to make sure that the id is a valid DD
        if (!displayDeviceRepository.existsById(id)) {
            return "Did not sent to DD, as it is not found in DB";
        }
        DisplayDeviceEntity dd = displayDeviceRepository.findById(id).get(); // Gets the DD from the DB

        // Set up the email data
        EmailDetailsEntity email = EmailDetailsEntity.builder()
                .msgBody("<b>Screen with the following information has disconnected:</b><br>"
                        + dd.toStringWithoutTSAndFallback())
                .subject("Disconnected Screen: " + id)
                .build();

        // Findes all user where that should get a notification and sends a email to them
        Set<UserEntity> userList = userRepository.findUserWithNotificationsEnabled();
        userList = filterUsersOutsidePausePeriod(userList); // filters user outside of perioed
        String mailResult = "Result from senting disconnect mail on id: " + id + " ";
        for (UserEntity user : userList) {
            try {
                email.setRecipient(user.getEmail());
                mailResult += "(" + sendSimpleMail(email) + ")";

            } catch (Exception e) {
                mailResult += "(Error: " + e.getMessage() + ")";
            }
        }
        return mailResult;
    }

    // This function takes in a set of User and filters them based on if they are outside of a notification pause period
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
