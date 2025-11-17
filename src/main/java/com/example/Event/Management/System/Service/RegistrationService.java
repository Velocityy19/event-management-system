package com.example.Event.Management.System.Service;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Models.Registration;
import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Repository.EventRepository;
import com.example.Event.Management.System.Repository.RegistrationRepository;
import com.example.Event.Management.System.Repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MailService mailService;

    //Check user registration
    public boolean isUserRegistered(String email, Long eventId) {

        Users user = usersRepository.findByEmail(email);
        if (user == null) return false;

        return registrationRepository.existsByUser_IdAndEvent_Id(user.getId(), eventId);
    }

    //Save user registration
    public Registration save(Registration reg) {

        reg.setRegisteredAt(LocalDateTime.now());
        Registration saved = registrationRepository.save(reg);

        mailService.sendRegistrationMail(
                reg.getUser().getEmail(),
                reg.getEvent().getTitle()
        );

        return saved;
    }

    //Save user registration for an event
    public void saveRegistration(Long userId, Long eventId) {

    Users user = usersRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

    Registration reg = new Registration();
    reg.setUser(user);
    reg.setEvent(event);
    reg.setRegisteredAt(LocalDateTime.now());

    registrationRepository.save(reg);

    mailService.sendRegistrationMail(user.getEmail(), event.getTitle());
}


    // Find user by Id in registrations
    public List<Registration> findByUserId(Long userId) {
        return registrationRepository.findByUser_Id(userId);
    }

    public List<Registration> findByEventId(Long eventId) {
    return registrationRepository.findByEvent_Id(eventId);
}

// Mark attendance 
    public void markAttendance(Long registrationId, boolean attended) {
    Registration reg = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration not found"));
    reg.setAttended(attended);
    registrationRepository.save(reg);
}

}
