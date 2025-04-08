package com.demoProj.demoProject.services;

import com.demoProj.demoProject.models.CartItem;
import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;


    public List<CartItem> getCartItemsByUserId(long userId) {
        return cartRepository.getCartItemsByUserId(userId);
    }


    public void addToCart(long userId, long productId, int quantity) {
        cartRepository.addToCart(userId, productId, quantity);
    }

    public void updateCartQuantity(long userId, long productId, int quantity) {
        if (quantity <= 0) {
            cartRepository.removeFromCart(userId, productId); // Remove item if quantity is zero
        } else {
            cartRepository.updateCartQuantity(userId, productId, quantity);
        }
    }

    public void removeFromCart(long userId, long productId) {
        cartRepository.removeFromCart(userId, productId);
    }

    public void clearCart(long userId) {
        cartRepository.clearCart(userId);
    }

    public void orderCart(Long userId) {
        cartRepository.orderCartItems(userId);
    }
}
