package com.demoProj.demoProject.controllers;

import com.demoProj.demoProject.models.CartItem;
import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("")
    public String showCart(Model model, HttpSession session) {

        // Ensure user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUsername");
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (loggedInUser == null || userId == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);

        model.addAttribute("cartItems", cartItems);
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }


    @PostMapping("/add")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        System.out.println("userId = " + userId);
        if (userId == null) {
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("addedToCart", "Product has been successfully added to the cart");
        cartService.addToCart(userId, productId, quantity);
        return "redirect:/products";
    }


    @PostMapping("/update")
    public String updateCartQuantity(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartService.updateCartQuantity(userId, productId, quantity);
        return "redirect:/cart";
    }

    /**
     * Remove a product from the cart
     */
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartService.removeFromCart(userId, productId);
        return "redirect:/cart";
    }


    @PostMapping("/order")
    public String order(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        cartService.orderCart(userId);
        model.addAttribute("placedOrder","Order has been placed successfully");
        return "cart";
    }


}
