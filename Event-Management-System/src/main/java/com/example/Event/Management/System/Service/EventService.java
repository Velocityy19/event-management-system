package com.example.Event.Management.System.Service;

import com.example.Event.Management.System.Models.Event;
import com.example.Event.Management.System.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    //Save event 
    public Event saveEvent(Event e) {
        return eventRepository.save(e);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get upcoming event
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateAfterOrderByDateAsc(LocalDate.now().minusDays(1));
    }

    //Find event by id
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    //delete Event by id 
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    // Find event by category
    public List<Event> findByCategory(String category) {
        return eventRepository.findByCategoryContainingIgnoreCase(category);
    }

    //Get event by id
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // search all events 
    public List<Event> searchEvents(String keyword) {
         return eventRepository.findByTitleContainingIgnoreCase(keyword);
    }




}
