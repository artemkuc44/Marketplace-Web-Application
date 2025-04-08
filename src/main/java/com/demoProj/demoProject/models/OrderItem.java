package com.demoProj.demoProject.models;

import java.time.LocalDateTime;

public class OrderItem {
    private long orderItemId;
    private long orderId;      // Link to the overall order
    private long productId;
    private String productName; // Optional, if you want to store product info at time of purchase
    private double boughtAtPrice;
    private int quantity;
    private double totalPrice;
    private LocalDateTime boughtAtTime;
    private boolean delivered;

    public OrderItem() {}

    public OrderItem(long orderItemId,
                     long orderId,
                     long productId,
                     String productName,
                     double boughtAtPrice,
                     int quantity,
                     double totalPrice,
                     LocalDateTime boughtAtTime,
                     boolean delivered) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.boughtAtPrice = boughtAtPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.boughtAtTime = boughtAtTime;
        this.delivered = delivered;
    }

    // Getters & Setters

    public long getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getBoughtAtPrice() {
        return boughtAtPrice;
    }
    public void setBoughtAtPrice(double boughtAtPrice) {
        this.boughtAtPrice = boughtAtPrice;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getBoughtAtTime() {
        return boughtAtTime;
    }

    public void setBoughtAtTime(LocalDateTime boughtAtTime) {
        this.boughtAtTime = boughtAtTime;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public boolean isDelivered() {
        return delivered;
    }
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }


}
