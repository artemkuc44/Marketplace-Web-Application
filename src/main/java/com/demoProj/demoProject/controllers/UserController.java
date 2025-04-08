package com.demoProj.demoProject.controllers;

import com.demoProj.demoProject.models.CartItem;
import com.demoProj.demoProject.models.Order;
import com.demoProj.demoProject.models.OrderItem;
import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.services.CartService;
import com.demoProj.demoProject.services.OrderService;
import com.demoProj.demoProject.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
