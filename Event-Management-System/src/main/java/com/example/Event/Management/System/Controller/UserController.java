package com.example.Event.Management.System.Controller;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Models.Registration;
import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Service.EventService;
import com.example.Event.Management.System.Service.RegistrationService;
import com.example.Event.Management.System.Service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    // get logged-in email 
    @GetMapping("/user/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("username");

        if (email == null) {
            return "redirect:/login";
        }

        Users user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }

        // upcoming events 
        List<Event> upcoming = eventService.getUpcomingEvents();
        model.addAttribute("upcomingEvents", upcoming);
        List<Registration> regs = registrationService.findByUserId(user.getId());
        List<Event> registeredEvents = regs.stream()
                .map(Registration::getEvent)
                .collect(Collectors.toList());
        model.addAttribute("registeredEvents", registeredEvents);

        // event recommendation
        List<Long> regEventIds = registeredEvents.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        List<Event> recommended = upcoming.stream()
                .filter(e -> !regEventIds.contains(e.getId()))
                .limit(4)
                .collect(Collectors.toList());
        model.addAttribute("recommendedEvents", recommended);

        // profile summary
        model.addAttribute("profileName", user.getName());
        model.addAttribute("profileEmail", user.getEmail());

        return "user-dashboard";
    }

    // User registered events
    @GetMapping("/user/registered-events")
public String registeredEvents(HttpSession session, Model model) {

    String email = (String) session.getAttribute("username");
    if (email == null) {
        return "redirect:/login";
    }

    Users user = userService.findByEmail(email);
    if (user == null) {
        return "redirect:/login";
    }

    List<Registration> regs = registrationService.findByUserId(user.getId());
    List<Event> registeredEvents = regs.stream()
            .map(Registration::getEvent)
            .collect(java.util.stream.Collectors.toList());

    model.addAttribute("registeredEvents", registeredEvents);
    model.addAttribute("profileName", user.getName());

    return "registered-events";
}

}
