package com.example.Event.Management.System.Controller;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Models.Registration;
import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Service.EventService;
import com.example.Event.Management.System.Service.RegistrationService;
import com.example.Event.Management.System.Service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    //Get event details
    @GetMapping("/{id}")
    public String eventDetails(@PathVariable Long id, HttpSession session, Model model) {

        String email = (String) session.getAttribute("username");

        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        boolean alreadyRegistered = false;

        if (email != null) {
            alreadyRegistered = registrationService.isUserRegistered(email, id);
        }

        model.addAttribute("event", event);
        model.addAttribute("alreadyRegistered", alreadyRegistered);

        return "event-details";
    }


    // Register for event
    @PostMapping("/register")
    public String registerToEvent(@RequestParam("eventId") Long eventId,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) return "redirect:/login";

        Users user = userService.findByEmail(userDetails.getUsername());
        Optional<Event> opt = eventService.findById(eventId);

        if (opt.isEmpty() || user == null) {
            return "redirect:/events";
        }

        if (!registrationService.isUserRegistered(user.getEmail(), eventId)) {
            Registration reg = new Registration();
            reg.setEvent(opt.get());
            reg.setUser(user);
            registrationService.save(reg);
        }

        return "redirect:/events/" + eventId;
    }


    // show available events 
    @GetMapping
    public String listEvents(Model model,
        @RequestParam(value = "q", required = false) String q,
        @RequestParam(value = "category", required = false) String category) {

    if (q != null && !q.isEmpty()) {
        model.addAttribute("events", eventService.searchEvents(q));
    } 
    else if (category != null && !category.isEmpty()) {
        model.addAttribute("events", eventService.findByCategory(category));
    } 
    else {
        model.addAttribute("events", eventService.getUpcomingEvents());
    }

    return "events";
    }

}
