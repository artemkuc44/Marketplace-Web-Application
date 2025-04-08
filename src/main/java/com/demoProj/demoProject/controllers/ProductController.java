package com.demoProj.demoProject.controllers;

import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("")
    public String showProducts(Model model, HttpSession session) {

        // Ensure user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (loggedInUser == null || userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", loggedInUser);
        model.addAttribute("products", productService.getAllNonHiddenProducts());
        return "products";
    }

    //Get req for productDetails page
    @GetMapping("/productDetails/{id}")
    public String getProductDetails(@PathVariable Long id, Model model, HttpSession session) {
        // Ensure user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (loggedInUser == null || userId == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(id);
        System.out.println(product.toString());
        model.addAttribute("product", product);
        return "productDetails";
    }



}
