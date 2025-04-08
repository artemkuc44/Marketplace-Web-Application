package com.demoProj.demoProject.controllers;

import com.demoProj.demoProject.models.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.demoProj.demoProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    public UserService userService;

    @PostMapping("/process")
    public String processLogIn(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) { // Inject HttpSession

        System.out.println("Received login username: " + username);
        System.out.println("Received login password: " + password);

        if (!userService.userExists(username) || !userService.correctPassword(username, password)) {
            System.out.println("Incorrect username or password");

            if (!userService.userExists(username)) {
                model.addAttribute("error", "No user found with username: " + username);
            } else {
                model.addAttribute("error", "Incorrect password");
            }
            return "login"; // Return login page with error message
        }

        model.addAttribute("success", "Successfully logged in as: " + username);

        // Store user details in session
        System.out.println(userService.getUserByUsername(username).getId().toString());
        session.setAttribute("loggedInUserId", userService.getUserByUsername(username).getId());
        session.setAttribute("loggedInUsername", username);
        session.setAttribute("loggedInRole", userService.getUserByUsername(username).getRole().toString());

        if (userService.getUserByUsername(username).getRole().equals(Role.MASTER)) {
            return "redirect:/master"; // Redirect to master dashboard
        } else {
            return "redirect:/products"; // Redirect to user dashboard
        }
    }



    @GetMapping("")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session to log out the user
        session.invalidate();
        System.out.println("User logged out successfully.");
        return "redirect:/login"; // Redirect to login page after logout
    }


}
