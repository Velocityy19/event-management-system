package com.example.Event.Management.System.Repository;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Models.Registration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RegistrationRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    void testExistsByUserAndEvent() {

        Users user = new Users();
        user.setEmail("test@gmail.com");
        user = usersRepository.save(user);

        Event event = new Event();
        event.setTitle("Concert");
        event = eventRepository.save(event);

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setEvent(event);
        registrationRepository.save(reg);

        boolean exists = registrationRepository.existsByUser_IdAndEvent_Id(
                user.getId(),
                event.getId()
        );

        assertTrue(exists);
    }
}
