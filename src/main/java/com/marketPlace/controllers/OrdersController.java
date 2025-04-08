package com.demoProj.demoProject.controllers;


import com.demoProj.demoProject.services.OrderService;
import com.demoProj.demoProject.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String showLast10Orders(Model model, HttpSession session) {
        // Ensure user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (loggedInUser == null || userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("last10Orders", orderService.get10OrderItemsByUserId(userId));
        return "orders";
    }

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

    @GetMapping("/orderDetails/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model, HttpSession session) {

        // Ensure user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (loggedInUser == null || userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("orderItems", orderService.getOrderItemsByOrderId(id));

        return "orderDetails";
    }





}
