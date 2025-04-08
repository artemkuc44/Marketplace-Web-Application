package com.demoProj.demoProject.repositories;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        // Order Items table: one record per product in the order
        String sqlOrderItems = "CREATE TABLE IF NOT EXISTS order_items (" +
                "order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "order_id BIGINT NOT NULL, " +
                "product_id BIGINT NOT NULL, " +
                "bought_at_price DOUBLE NOT NULL, " +
                "quantity INT NOT NULL, " +
                "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE" +
                ");";
        jdbcTemplate.execute(sqlOrderItems);
    }
}
