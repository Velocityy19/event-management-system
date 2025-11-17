package com.example.Event.Management.System.Controller;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Service.EventService;
import com.example.Event.Management.System.Service.RegistrationService;

import jakarta.servlet.http.HttpSession;
import com.example.Event.Management.System.Models.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/events")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired 
    private RegistrationService registrationService;

    // Verify admin
    private boolean isNotAdmin(HttpSession session) {
        String email = (String) session.getAttribute("username");
        return (email == null || !email.equalsIgnoreCase("admin@gmail.com"));
    }

    @GetMapping
    public String listEvents(HttpSession session, Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/home";
        }

        model.addAttribute("events", eventService.getAllEvents());
        return "admin-events";
    }

    // Add an event
    @GetMapping("/add")
    public String addEventForm(HttpSession session, Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/home";
        }

        model.addAttribute("event", new Event());
        return "add-event";
    }

    //Save an event
    @PostMapping("/save")
    public String saveEvent(@ModelAttribute("event") Event event, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/home";
        }

        eventService.saveEvent(event);
        return "redirect:/admin/events";
    }

    //Edit event
    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, HttpSession session, Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/home";
        }

        Optional<Event> opt = eventService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("event", opt.get());
            return "edit-event";
        }

        return "redirect:/admin/events";
    }


    //update event 
    @PostMapping("/update")
    public String updateEvent(@ModelAttribute("event") Event event, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/home";
        }

        eventService.saveEvent(event);
        return "redirect:/admin/events";
    }

    //delete event
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/home";
        }

        eventService.deleteById(id);
        return "redirect:/admin/events";
    }

    //check attendance
   @GetMapping("/{id}/registrations")
    public String viewEventRegistrations(@PathVariable Long id, Model model) {

    Event event = eventService.getEventById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));

    List<Registration> regs = registrationService.findByEventId(id);

    model.addAttribute("event", event);
    model.addAttribute("registrations", regs);

    return "admin-event-registrations";
}

    //update attendance
    @PostMapping("/attendance/update-all")
    public String updateAllAttendance(
        @RequestParam("regIdList") List<Long> regIds,
        @RequestParam("attendedList") List<Boolean> attendedList,
        @RequestParam Long eventId) {

    for (int i = 0; i < regIds.size(); i++) {
        registrationService.markAttendance(regIds.get(i), attendedList.get(i));
    }

    return "redirect:/admin/events";
    }
}
