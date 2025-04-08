package com.demoProj.demoProject.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute("userRole")
    public String getUserRole(HttpSession session) {
        Object role = session.getAttribute("loggedInRole");
        return role != null ? role.toString() : "USER"; // default role if none is found
    }
}
