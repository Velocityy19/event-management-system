package com.example.Event.Management.System.Controller;

import com.example.Event.Management.System.Service.EventService;
import com.example.Event.Management.System.Service.RegistrationService;
import com.example.Event.Management.System.Models.Event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false) 
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private RegistrationService registrationService;

    private MockHttpSession adminSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "admin@gmail.com");
        return session;
    }

    @Test
    void adminAccessAllowed() throws Exception {
        when(eventService.getAllEvents()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/events")
                        .session(adminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-events"));
    }

    @Test
    void adminAccessDeniedForNormalUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "user@gmail.com");

        mockMvc.perform(get("/admin/events")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testViewRegistrations() throws Exception {
        Event ev = new Event();
        ev.setId(1L);
        ev.setTitle("Test Event");

        when(eventService.getEventById(1L)).thenReturn(Optional.of(ev));
        when(registrationService.findByEventId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/events/1/registrations")
                        .session(adminSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-event-registrations"));
    }
}
