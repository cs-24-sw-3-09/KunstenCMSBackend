package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.UserRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.Setter;

@Setter
@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;

    private DisplayDeviceService displayDeviceService;

    private UserRepository userRepository;

    @Value("${EMAIL.USERNAME:}")
    private String sender;

    @Autowired
    public EmailServiceImpl(DisplayDeviceService displayDeviceService, JavaMailSender javaMailSender,
            UserRepository userRepository) {
        this.displayDeviceService = displayDeviceService;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendSimpleMail(EmailDetailsEntity details) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Use MimeMessageHelper to set up the message. This is do to the email needed to be HTML
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(details.getMsgBody(), true);

            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully To " + details.getRecipient();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail..." + e.getMessage();
        }
    }

    @Override
    public String sendDDDisconnectMail(int id) {
        Time currentTime = Time.valueOf(LocalTime.now(ZoneId.of("Europe/Copenhagen")));
        return sendDDDisconnectMail(id, currentTime);
    }

    @Override
    public String sendDDDisconnectMail(int id, Time currentTime) {
        // Checks that the ID is in the database. In order to make sure that the id is a valid DD
        if (!displayDeviceService.isExists((long) id)) {
            return "Did not sent mail, as DD is not found in DB";
        }
        DisplayDeviceEntity dd = displayDeviceService.findOne((long) id).get();

        // Making sure that it should be on
        if (!shallDDSendMailForWeek(dd, currentTime, LocalDate.now(ZoneId.of("Europe/Copenhagen")).getDayOfWeek()))
            return "Did not sent mail, as DD should be off";

        // Set up the email data
        EmailDetailsEntity email = EmailDetailsEntity.builder()
                .msgBody("<b>Screen with the following information has disconnected:</b><br>"
                        + dd.toStringWithoutTSAndFallback())
                .subject("Disconnected Screen: " + id)
                .build();

        // Findes all user where that should get a notification and sends a email to them
        Set<UserEntity> userList = userRepository.findUserWithNotificationsEnabled();
        userList = filterUsersOutsidePausePeriod(userList); // filters of user with noficications paused
        // Send mails and construct return status
        String mailResult = "Result from senting disconnect mail on id: " + id + " ";
        for (UserEntity user : userList) {
            email.setRecipient(user.getEmail());
            mailResult += "(" + sendSimpleMail(email) + ")";
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

    private static boolean shallDDSendMailForWeek(DisplayDeviceEntity dd, Time currentTime, DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> shallDDSendMailForSingleDay(dd.getMonday_start(), dd.getMonday_end(), currentTime);
            case TUESDAY-> shallDDSendMailForSingleDay(dd.getTuesday_start(), dd.getTuesday_end(), currentTime);
            case WEDNESDAY -> shallDDSendMailForSingleDay(dd.getWednesday_start(), dd.getWednesday_end(), currentTime);
            case THURSDAY -> shallDDSendMailForSingleDay(dd.getThursday_start(), dd.getThursday_end(), currentTime);
            case FRIDAY -> shallDDSendMailForSingleDay(dd.getFriday_start(), dd.getFriday_end(), currentTime);
            case SATURDAY -> shallDDSendMailForSingleDay(dd.getSaturday_start(), dd.getSaturday_end(), currentTime);
            case SUNDAY -> shallDDSendMailForSingleDay(dd.getSunday_start(), dd.getSunday_end(), currentTime);
        };
    }

    private static boolean shallDDSendMailForSingleDay(Time start, Time end, Time currentTime) {
        // If either start or end is null, consider it to not send a mail
        if (start == null || end == null) {
            return false;
        }

        // If the current time is after the start and before the end the screen should be on and therefore the system shall send a mail
        return currentTime.after(start) && currentTime.before(end);
    }

}
