package com.example.Event.Management.System.Service;
import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    public EventServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEvent() {
        Event event = new Event();
        event.setTitle("Tech Summit");

        when(eventRepository.save(event)).thenReturn(event);

        Event saved = eventService.saveEvent(event);
        assertEquals("Tech Summit", saved.getTitle());
    }

    @Test
    void testGetEventById() {
        Event event = new Event();
        event.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> found = eventService.getEventById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(new Event(), new Event()));

        assertEquals(2, eventService.getAllEvents().size());
    }

    @Test
    void testDeleteEvent() {
        doNothing().when(eventRepository).deleteById(1L);

        eventService.deleteById(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }
}