package com.example.Event.Management.System.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.Event.Management.System.Models.Users;
import com.example.Event.Management.System.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {
    @Autowired
    UserService userService;

     // Land at home page
    @GetMapping("/")
    public String landingPage() {
        return "home";
    }

    // Land at home page
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    
    //show Login page 
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    // Show register page
    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new Users());
        return "register";
    }

    //Save user 
    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute("user") Users user ){
        userService.saveUser(user);
        return "redirect:/login";
    }

}
