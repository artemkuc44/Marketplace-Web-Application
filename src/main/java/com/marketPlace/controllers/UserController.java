package com.marketPlace.controllers;

import com.marketPlace.services.OrderService;
import com.marketPlace.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;  // Add this line

    @PostMapping("/order")
    public String orderItem(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        // Get userId from session (secure method)
        Long userId = (Long) session.getAttribute("loggedInUserId");
        System.out.println("product id in controller" + productId.toString());
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        // Place the order
        orderService.orderItem(userId, productService.getProductById(productId), quantity);

        return "redirect:/orders"; // Redirect to order history page
    }


}
