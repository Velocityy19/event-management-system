package com.example.Event.Management.System.Repository;

import com.example.Event.Management.System.Models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDateAfterOrderByDateAsc(LocalDate date);
    List<Event> findByCategoryContainingIgnoreCase(String category);
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByCategoryIgnoreCase(String category);
    


}
