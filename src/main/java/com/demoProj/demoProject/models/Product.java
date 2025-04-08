package com.demoProj.demoProject.models;

import jakarta.persistence.*;

public class Product {

    private Long id;

    private String name;
    private double price;


    private String description;
    private Boolean hidden;


    public Product() {}
    public Product(Long id, String name, double price) {
        this.name = name;
        this.price = price;
        this.id = id;

        this.description = "";
        this.hidden = false;
    }

    public Product(Long id, String name, double price, String description, Boolean hidden) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.description = description;
        this.hidden = hidden;
    }

    public Product(Long id, String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.description = description;

    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getHidden() { return hidden; }
    public void setHidden(Boolean hidden) { this.hidden = hidden; }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price  + ", description=" + description + "]";
    }
}
