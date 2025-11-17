package com.example.Event.Management.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Event.Management.System.Models.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
    Users findByEmail(String email);
}
