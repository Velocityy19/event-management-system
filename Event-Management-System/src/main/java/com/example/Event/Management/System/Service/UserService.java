package com.example.Event.Management.System.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Repository.UsersRepository;

@Service
public class UserService {
    @Autowired
    UsersRepository usersRepository;

    //BCrypt Password
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

   // Save user
   public void saveUser(Users user){
    user.setPassword(encoder.encode(user.getPassword()));
    usersRepository.save(user);
   } 

   // Fetch User by email and password
   public Users fetchUser(String email, String password){
    return usersRepository.findByEmail(email);
   }

   //Find users by email
   public Users findByEmail(String email) {
    return usersRepository.findByEmail(email);
   }
}
