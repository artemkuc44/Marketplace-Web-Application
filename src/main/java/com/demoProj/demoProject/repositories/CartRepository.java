package com.demoProj.demoProject.repositories;

import com.demoProj.demoProject.models.CartItem;
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
import java.util.List;

@Repository
public class CartRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    public void initializeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS carts ("
                + "user_id BIGINT NOT NULL, "
                + "product_id BIGINT NOT NULL, "
                + "quantity INT NOT NULL DEFAULT 1, "
                + "PRIMARY KEY (user_id, product_id), "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE"
                + ");";

        jdbcTemplate.execute(sql);
    }



    public List<CartItem> getCartItemsByUserId(long userId) {
        String sql = "SELECT p.id AS product_id, p.productName, p.price, c.quantity AS cart_quantity " +
                "FROM products p " +
                "JOIN carts c ON p.id = c.product_id " +
                "WHERE c.user_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new CartItem(
                        new Product(
                                rs.getLong("product_id"),
                                rs.getString("productName"),
                                rs.getDouble("price")),
                        rs.getInt("cart_quantity")// Quantity from cart
                )
        );
    }


    /**
     * Add product to the cart or update quantity if it exists.
     */
    public int addToCart(long userId, long productId, int quantity) {
        String sql = "INSERT INTO carts (user_id, product_id, quantity) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        return jdbcTemplate.update(sql, userId, productId, quantity, quantity);
    }

    /**
     * Remove a product from the user's cart.
     */
    public int removeFromCart(long userId, long productId) {
        String sql = "DELETE FROM carts WHERE user_id = ? AND product_id = ?";
        return jdbcTemplate.update(sql, userId, productId);
    }


    public int clearCart(long userId) {
        String sql = "DELETE FROM carts WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    /**
     * Update the quantity of a product in the cart.
     */
    public int updateCartQuantity(long userId, long productId, int quantity) {
        String sql = "UPDATE carts SET quantity = ? WHERE user_id = ? AND product_id = ?";
        return jdbcTemplate.update(sql, quantity, userId, productId);
    }


    @Transactional
    public void orderCartItems(Long userId) {
        // Retrieve all cart items for the user.
        String getCartItemsSql = "SELECT * FROM carts WHERE user_id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(getCartItemsSql, new Object[]{userId}, (rs, rowNum) ->
                new CartItem(productRepository.findById(rs.getLong("product_id")),
                        rs.getInt("quantity"))
        );

        // If no items are found, there's nothing to order.
        if (cartItems.isEmpty()) {
            return;
        }

        // Calculate the total price.
        double totalPrice = cartItems.stream().mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity()).sum();

        // Insert a new order into the orders table and retrieve the generated order_id.
        String insertOrderSql = "INSERT INTO orders (user_id, totalBAPrice) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            // Create a PreparedStatement with the SQL and request generated keys.
            PreparedStatement ps = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);      // Set user_id parameter.
            ps.setDouble(2, totalPrice); // Set totalBAPrice parameter.
            return ps;
        }, keyHolder);

        // Retrieve the generated order_id from the KeyHolder.
        Long orderId = keyHolder.getKey().longValue();

        // Insert each cart item into the order_items table, linking them to the order using orderId.
        String insertOrderItemSql = "INSERT INTO order_items (order_id, product_id, bought_at_price, quantity) VALUES (?, ?, ?, ?)";
        for (CartItem cartItem : cartItems) {
            jdbcTemplate.update(insertOrderItemSql,
                    orderId,                                      // Generated order ID.
                    cartItem.getProduct().getId(),                // Product ID.
                    cartItem.getProduct().getPrice(),             // Price at time of order.
                    cartItem.getQuantity()                        // Quantity ordered.
            );
        }

        // Clear the user's cart.
        String deleteSql = "DELETE FROM carts WHERE user_id = ?";
        jdbcTemplate.update(deleteSql, userId);
    }



}
