package com.demoProj.demoProject.services;


import com.demoProj.demoProject.models.Order;
import com.demoProj.demoProject.models.OrderItem;
import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {


    @Autowired
    private OrderRepository orderRepository;

    public List<Order> get10OrderItemsByUserId(long userId) {
        return orderRepository.find10ById(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void orderItem(Long userId, Product product, int quantity) {
       orderRepository.orderItem(userId, product, quantity);
    }

    public List<OrderItem> getOrderItemsByOrderId(long orderId) {
        return orderRepository.getOrderItems(orderId);
    }

    public void updateOrderStatus(long orderId, boolean delivered) {
        orderRepository.updateStatus(orderId, delivered);
    }
}
