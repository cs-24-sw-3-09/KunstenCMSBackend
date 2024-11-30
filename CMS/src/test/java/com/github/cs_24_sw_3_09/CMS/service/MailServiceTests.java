package com.github.cs_24_sw_3_09.CMS.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.UserRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.EmailService;
import com.github.cs_24_sw_3_09.CMS.services.UserService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.EmailServiceImpl;

import jakarta.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class MailServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private DisplayDeviceService displayDeviceService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailServiceImpl mailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendSimpleMailSuccess() {
        String mockSender = "sender@example.com";
        String mockReceiver = "receiver@example.com";

        mailService.setSender(mockSender);

        // Create a mock MimeMessage
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        EmailDetailsEntity emailDetails = EmailDetailsEntity.builder()
                .recipient(mockReceiver)
                .msgBody("Test Message")
                .subject("Test Subject")
                .build();

        String result = mailService.sendSimpleMail(emailDetails);

        // Verify the JavaMailSender was called
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mockMimeMessage);

        // Assert
        System.out.println("result: " + result);
        assertEquals("Mail Sent Successfully To " + mockReceiver, result);
    }

    @Test
    void testSendSimpleMailFailure() {
        String mockSender = "sender@example.com";
        String mockReceiver = "receiver@example.com";

        mailService.setSender(mockSender);

        // Create a mock MimeMessage
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        EmailDetailsEntity emailDetails = EmailDetailsEntity.builder()
                .recipient(mockReceiver)
                .msgBody("Test Message")
                .subject("Test Subject")
                .build();

        // Simulate exception when sending mail
        doThrow(new RuntimeException("Mail server is down")).when(javaMailSender).send(any(SimpleMailMessage.class));

        String result = mailService.sendSimpleMail(emailDetails);

        // Assert
        assertTrue(result.contains("Error while Sending Mail..."));
    }

    @Test
    void testSendDDDisconnectMail() throws Exception {
        // Mock user repository to return a user
        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setEmail("test@example.com");
        when(userRepository.findUserWithNotificationsEnabled())
                .thenReturn(Set.of(testUserEntity));

        // Mock display device service
        DisplayDeviceEntity displayDeviceEntity = new DisplayDeviceEntity();
        displayDeviceEntity.setId(1);
        displayDeviceEntity.setName("Test Device");
        Time start = Time.valueOf("08:00:00");
        Time end = Time.valueOf("16:00:00");
        displayDeviceEntity.setMonday_start(start);
        displayDeviceEntity.setMonday_end(end);
        displayDeviceEntity.setTuesday_start(start);
        displayDeviceEntity.setTuesday_end(end);
        displayDeviceEntity.setWednesday_start(start);
        displayDeviceEntity.setWednesday_end(end);
        displayDeviceEntity.setThursday_start(start);
        displayDeviceEntity.setThursday_end(end);
        displayDeviceEntity.setFriday_start(start);
        displayDeviceEntity.setFriday_end(end);
        displayDeviceEntity.setSaturday_start(start);
        displayDeviceEntity.setSaturday_end(end);
        displayDeviceEntity.setSunday_start(start);
        displayDeviceEntity.setSunday_end(end);
        when(displayDeviceService.isExists(1L)).thenReturn(true);
        when(displayDeviceService.findOne(1L)).thenReturn(Optional.of(displayDeviceEntity));

        // Mock MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Set the sender email
        String mockSender = "sender@example.com";
        mailService.setSender(mockSender);

        // Call the method under test
        String result = mailService.sendDDDisconnectMail(1, Time.valueOf("10:00:00"));

        // Verify interactions
        verify(displayDeviceService, times(1)).isExists(1L);
        verify(displayDeviceService, times(1)).findOne(1L);
        verify(userRepository, times(1)).findUserWithNotificationsEnabled();
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);

        // Assert the result
        assertEquals("Result from senting disconnect mail on id: 1 (Mail Sent Successfully To test@example.com)",
                result);
    }

    @Test
    void testSendDDDisconnectMailButNoDeviceWithId() throws Exception {
        String result = mailService.sendDDDisconnectMail(1);

        // Assert the result
        assertEquals("Did not sent mail, as DD is not found in DB",
                result);
    }

    @Test
    void testSendDDDisconnectMailButMailSendError() throws Exception {
        // Mock user repository to return a user
        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setEmail("test@example.com");
        when(userRepository.findUserWithNotificationsEnabled())
                .thenReturn(Set.of(testUserEntity));

        // Mock display device service
        DisplayDeviceEntity displayDeviceEntity = new DisplayDeviceEntity();
        displayDeviceEntity.setId(1);
        displayDeviceEntity.setName("Test Device");
        Time start = Time.valueOf("08:00:00");
        Time end = Time.valueOf("16:00:00");
        displayDeviceEntity.setMonday_start(start);
        displayDeviceEntity.setMonday_end(end);
        displayDeviceEntity.setTuesday_start(start);
        displayDeviceEntity.setTuesday_end(end);
        displayDeviceEntity.setWednesday_start(start);
        displayDeviceEntity.setWednesday_end(end);
        displayDeviceEntity.setThursday_start(start);
        displayDeviceEntity.setThursday_end(end);
        displayDeviceEntity.setFriday_start(start);
        displayDeviceEntity.setFriday_end(end);
        displayDeviceEntity.setSaturday_start(start);
        displayDeviceEntity.setSaturday_end(end);
        displayDeviceEntity.setSunday_start(start);
        displayDeviceEntity.setSunday_end(end);

        when(displayDeviceService.isExists(1L)).thenReturn(true);
        when(displayDeviceService.findOne(1L)).thenReturn(Optional.of(displayDeviceEntity));

        // Mock MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Set the sender email
        String mockSender = "sender@example.com";
        mailService.setSender(mockSender);
        doThrow(new RuntimeException("Mail server is down")).when(javaMailSender).send(any(SimpleMailMessage.class));
        // Call the method under test
        Time currentTime = Time.valueOf("12:00:00");
        String result = mailService.sendDDDisconnectMail(1, currentTime);

        System.out.println(result);
        // Assert the result
        assertEquals(result.contains("Result from senting disconnect mail on id: 1 (Error while Sending Mail..."),
                true);
    }

    @Test
    void unitTestFor_filterUsersOutsidePausePeriod() throws Exception {
        // Mock user repository to return a user
        Set<UserEntity> setUsers = new HashSet<>();

        // Normal User
        UserEntity testUserEntity = TestDataUtil.createUserEntity();
        setUsers.add(testUserEntity);

        // User with perioded
        testUserEntity = TestDataUtil.createUserEntity();
        Calendar calendar = Calendar.getInstance();

        // Set `pauseNotificationStart` to 2 days before today
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        testUserEntity.setPauseNotificationStart(new Date(calendar.getTimeInMillis()));

        // Reset the calendar and set `pauseNotificationEnd` to 2 days after today
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        testUserEntity.setPauseNotificationEnd(new Date(calendar.getTimeInMillis()));
        setUsers.add(testUserEntity);

        setUsers = mailService.filterUsersOutsidePausePeriod(setUsers);
        assertEquals(setUsers.size(), 1);
    }

    @Test
    void unitTestFor_shallDDSendMailForWeek() throws Exception {
        EmailServiceImpl emailServiceImpl = new EmailServiceImpl(null, null, null);
        DisplayDeviceEntity dd = new DisplayDeviceEntity();
        Time currentTime = Time.valueOf("12:00:00");

        // Access the private method via reflection
        Method method = EmailServiceImpl.class.getDeclaredMethod(
                "shallDDSendMailForWeek", DisplayDeviceEntity.class, Time.class, DayOfWeek.class);
        method.setAccessible(true);

        // Iterate over all days of the week
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            boolean result = (boolean) method.invoke(emailServiceImpl, dd, currentTime, dayOfWeek);
            assertFalse(result, "Expected false for day: " + dayOfWeek);
        }
    }

    @Test
    void shallDDSendMailForSingleDay() throws Exception {
        EmailServiceImpl emailServiceImpl = new EmailServiceImpl(null, null, null);

        // Access the private method via reflection
        Method method = EmailServiceImpl.class.getDeclaredMethod(
                "shallDDSendMailForSingleDay", Time.class, Time.class, Time.class);
        method.setAccessible(true);

        // case: 1
        Time start = null;
        Time end = null;
        Time currentTime = Time.valueOf("12:00:00");
        boolean result = (boolean) method.invoke(emailServiceImpl, start, end, currentTime);
        assertFalse(result);

        // case: 2
        start = null;
        end = Time.valueOf("13:00:00");
        currentTime = Time.valueOf("12:00:00");
        result = (boolean) method.invoke(emailServiceImpl, start, end, currentTime);
        assertFalse(result);

        // case: 3
        start = Time.valueOf("13:00:00");
        end = null;
        currentTime = Time.valueOf("12:00:00");
        result = (boolean) method.invoke(emailServiceImpl, start, end, currentTime);
        assertFalse(result);

        // case: 4
        start = Time.valueOf("13:00:00");
        end = Time.valueOf("14:00:00");
        currentTime = Time.valueOf("12:00:00");
        result = (boolean) method.invoke(emailServiceImpl, start, end, currentTime);
        assertFalse(result);

        // case: 5
        start = Time.valueOf("10:00:00");
        end = Time.valueOf("11:00:00");
        currentTime = Time.valueOf("12:00:00");
        result = (boolean) method.invoke(emailServiceImpl, start, end, currentTime);
        assertFalse(result);

        // case: 6
        start = Time.valueOf("10:00:00");
        end = Time.valueOf("13:00:00");
        currentTime = Time.valueOf("12:00:00");
        result = (boolean) method.invoke(emailServiceImpl, start, end, currentTime);
        assertTrue(result);
    }
}
