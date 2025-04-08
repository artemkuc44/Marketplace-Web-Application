package com.demoProj.demoProject.repositories;

import com.demoProj.demoProject.models.Order;
import com.demoProj.demoProject.models.OrderItem;
import com.demoProj.demoProject.models.Product;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates the orders table if it doesn't exist.
     */
    @PostConstruct
    public void initializeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS orders (" +
                "order_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id BIGINT NOT NULL, " +
                "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                "totalBAPrice DOUBLE NOT NULL, " +
                "delivered BOOLEAN DEFAULT FALSE NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ");";
        jdbcTemplate.execute(sql);
    }


    /**
     * Fetch the last 10 orders for a specific user, including boughtAtPrice.
     */
    public List<Order> find10ById(long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC LIMIT 10";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new Order(
                        rs.getLong("order_id"),                       // Use "order_id"
                        rs.getLong("user_id"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        rs.getDouble("totalBAPrice"),                   // Use "totalBAPrice" (matches the DB)
                        rs.getInt("delivered") == 1
                )
        );
    }


    /**
     * Fetch all orders, including boughtAtPrice.
     */
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders;";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Order(
                        rs.getLong("order_id"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        rs.getDouble("totalBAPrice"),
                        rs.getInt("delivered") == 1
                )
        );
    }

    @Transactional
    public void orderItem(Long userId, Product product, int quantity) {
        // Calculate the total price for the order.
        double totalPrice = product.getPrice() * quantity;

        // 1. Insert a new order into the orders table and retrieve the generated order_id.
        String insertOrderSql = "INSERT INTO orders (user_id, totalBAPrice) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setDouble(2, totalPrice);
            return ps;
        }, keyHolder);
        Long orderId = keyHolder.getKey().longValue();

        // 2. Insert the order item into the order_items table.
        String insertOrderItemSql = "INSERT INTO order_items (order_id, product_id, bought_at_price, quantity) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertOrderItemSql,
                orderId,
                product.getId(),
                product.getPrice(),  // Price at time of ordering.
                quantity);

    }

    public List<OrderItem> getOrderItems(Long orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, " +
                "oi.bought_at_price, oi.quantity, o.order_date , o.delivered " +
                "FROM order_items AS oi " +
                "JOIN orders AS o ON oi.order_id = o.order_id " +
                "WHERE oi.order_id = ?";


        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                new OrderItem(
                        rs.getLong("order_item_id"),
                        rs.getLong("order_id"),
                        rs.getLong("product_id"),
                        productRepository.findById(rs.getLong("product_id")).getName(),
                        rs.getDouble("bought_at_price"),
                        rs.getInt("quantity"),
                        rs.getDouble("bought_at_price")*rs.getInt("quantity"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        rs.getInt("delivered") == 1
                )
        );
    }

    public void updateStatus(Long orderId, boolean delivered) {
        String sql = "UPDATE orders SET delivered = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, delivered, orderId);
    }

}

