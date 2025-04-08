package com.demoProj.demoProject.controllers;

import com.demoProj.demoProject.models.Order;
import com.demoProj.demoProject.models.OrderItem;
import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.models.User;
import com.demoProj.demoProject.services.OrderService;
import com.demoProj.demoProject.services.ProductService;
import com.demoProj.demoProject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/master")
public class MasterController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String showMasterDashboard(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        String role = (String) session.getAttribute("loggedInRole");

        if (loggedInUser == null || role == null || !role.equals("MASTER")) {
            return "redirect:/login";
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("username", loggedInUser);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);



        return "master";
    }

    @PostMapping("/product/add")
    public String addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            HttpSession session) {

        String role = (String) session.getAttribute("loggedInRole");
        if (role == null || !role.equals("MASTER")) {
            return "redirect:/login";
        }

        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        product.setDescription(description);
        productService.addProduct(product);

        return "redirect:/master";
    }

    @PostMapping("/product/update/{id}")
    public String updateProductQuantity(@PathVariable("id") Long productId,
                                        @RequestParam("price") double price,
                                        @RequestParam("description") String description,
                                        HttpSession session) {
        String role = (String) session.getAttribute("loggedInRole");
        if (role == null || !role.equals("MASTER")) {
            return "redirect:/login";
        }
        String unchangedNamed = productService.getProductById(productId).getName();

        productService.updateProduct(productId,unchangedNamed, price, description);
        return "redirect:/master";
    }


    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Long userId,
                             @RequestParam("username") String username,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam("role") String role,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        String loggedInRole = (String) session.getAttribute("loggedInRole");
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");

        // Prevent master (id=1) from being updated
        if (userId.equals(1L)) {
            redirectAttributes.addAttribute("Master1Error", "Cannot make changes to master with id 1");
            return "redirect:/master";
        }

        // Use existing password if not provided
        if (password == null || password.isBlank()){
            User existingUser = userService.getUserById(userId);
            password = existingUser.getPassword();
        }

        // Update user in the database
        userService.updateUser(userId, username, password, role);

        // If the currently logged-in user updated their own role, update the session
        if (loggedInUserId != null && loggedInUserId.equals(userId)) {
            session.setAttribute("loggedInRole", role);
            // If new role is not MASTER, redirect to products page
            if (!"MASTER".equals(role)) {
                return "redirect:/products";
            }
        }

        return "redirect:/master";
    }

    @PostMapping("/product/hide/{id}")
    public String deleteProduct(@PathVariable("id") Long productId, @RequestParam Boolean hidden, HttpSession session) {
        String role = (String) session.getAttribute("loggedInRole");
        if (role == null || !role.equals("MASTER")) {
            return "redirect:/login";
        }

        productService.hideById(productId, hidden);
        return "redirect:/master";
    }

    @PostMapping("/orders/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam boolean delivered, Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (loggedInUser == null || userId == null) {
            return "redirect:/login";
        }

        orderService.updateOrderStatus(id, delivered);

        return "redirect:/master";
    }
}