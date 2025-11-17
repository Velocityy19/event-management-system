package com.example.Event.Management.System.Service;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Models.Registration;
import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Repository.EventRepository;
import com.example.Event.Management.System.Repository.RegistrationRepository;
import com.example.Event.Management.System.Repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void testIsUserRegistered() {

        Users user = new Users();
        user.setId(1L);

        when(usersRepository.findByEmail("test@gmail.com"))
                .thenReturn(user);

        when(registrationRepository.existsByUser_IdAndEvent_Id(1L, 10L))
                .thenReturn(true);

        boolean result = registrationService.isUserRegistered("test@gmail.com", 10L);

        assertTrue(result);
    }

    @Test
    void testIsUserRegistered_UserNotFound() {

        when(usersRepository.findByEmail(anyString()))
                .thenReturn(null);

        boolean result = registrationService.isUserRegistered("missing@gmail.com", 10L);

        assertFalse(result);
    }

    @Test
    void testMarkAttendance() {

        Registration reg = new Registration();
        reg.setId(5L);
        reg.setAttended(false);

        when(registrationRepository.findById(5L))
                .thenReturn(Optional.of(reg));

        registrationService.markAttendance(5L, true);

        assertTrue(reg.isAttended());
        verify(registrationRepository).save(reg);
    }

    @Test
    void testSaveRegistration() {

        Users user = new Users();
        user.setEmail("user@gmail.com");

        Event event = new Event();
        event.setTitle("Sample Event");

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setEvent(event);

        when(registrationRepository.save(any(Registration.class)))
                .thenReturn(reg);

        registrationService.save(reg);

        verify(registrationRepository).save(any(Registration.class));
        verify(mailService).sendRegistrationMail("user@gmail.com", "Sample Event");
    }
}
