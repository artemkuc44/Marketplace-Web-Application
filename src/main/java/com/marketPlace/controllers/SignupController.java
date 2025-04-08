package com.demoProj.demoProject.controllers;

import com.demoProj.demoProject.models.Role;
import jakarta.servlet.ServletOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.demoProj.demoProject.models.User;
import com.demoProj.demoProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup") // Corrected mapping
public class SignupController {
    @Autowired
    private UserService userService;

    @PostMapping("/process")
    public String addUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Debugging: Print received data to console
        System.out.println("Received Username: " + username);
        System.out.println("Received Password: " + password);

        if (userService.userExists(username)) {
            System.out.println("Username already exists");
            model.addAttribute("error", "Username already exists!");
            return "signup";
        }
        else if(username.contains(" ")) {
            System.out.println("Username cannot contains spaces");
            model.addAttribute("error", "username cannot contains spaces!");
            return "signup";
        }
        System.out.println("Creating new user");
        User user = new User();
        System.out.println(user.getRole().toString());
        user.setUsername(username);
        user.setPassword(password);



        userService.register(user);

        return "redirect:/login";
    }

    @GetMapping("")
    public String showSignupForm() {
        return "signup";
    }



}
