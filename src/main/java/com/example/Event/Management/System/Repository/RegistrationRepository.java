package com.example.Event.Management.System.Repository;

import com.example.Event.Management.System.Models.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByEvent_Id(Long eventId);

    List<Registration> findByUser_Id(Long userId);

    boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);

    Object existsByUserIdAndEventId(long l, long m);
}
