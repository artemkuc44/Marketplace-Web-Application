package com.demoProj.demoProject.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private long orderId;
    private long userId;
    private LocalDateTime orderDate;
    private double totalPrice;
    private boolean delivered;

    // Optional: A list of order items to facilitate object relationships
    private List<OrderItem> orderItems;

    public Order() {}

    public Order(long orderId, long userId, LocalDateTime orderDate, double totalPrice, boolean delivered) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.delivered = delivered;
    }

    // Getters & Setters

    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isdelivered() {
        return delivered;
    }
    public void setdelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", delivered=" + delivered +
                ", orderItems=" + orderItems +
                '}';
    }
}
