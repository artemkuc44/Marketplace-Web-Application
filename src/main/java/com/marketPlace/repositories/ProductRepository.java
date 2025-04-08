package com.demoProj.demoProject.repositories;

import com.demoProj.demoProject.models.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "productName VARCHAR(255) UNIQUE NOT NULL, "
                + "price DOUBLE NOT NULL, "
                + "hidden BOOLEAN NOT NULL"
                + ")";
        jdbcTemplate.execute(sql);
    }

    public List<Product> findAllNonHidden() {
        String sql = "SELECT * FROM products WHERE hidden = false";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Product(rs.getLong("id"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getBoolean("hidden"))
        );
    }


    public List<Product> findAllProducts() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Product(rs.getLong("id"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getBoolean("hidden"))
        );
    }

    public Product updateProductId(Long productId, int quantity) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(sql, quantity, productId);

        if (updatedRows > 0) {
            String selectSql = "SELECT * FROM products WHERE id = ?";
            return jdbcTemplate.queryForObject(selectSql,
                    new Object[]{productId},
                    (rs, rowNum) -> new Product(
                            rs.getLong("id"),
                            rs.getString("productName"),
                            rs.getDouble("price"),
                            rs.getString("description")
                    )
            );
        }
        return null;
    }

    public int updateProduct(Long id, String productName, double price, String description) {
        String sql = "UPDATE products SET productName = ?, price = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, productName, price, description, id);
    }



    public int addProduct(Product product) {
        String sql = "INSERT INTO products (productName, price, description) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE price = VALUES(price)";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getDescription());
    }

    public int hideById(long id, boolean hidden) {
        String sql = "UPDATE products SET hidden = ? WHERE id = ?";
        return jdbcTemplate.update(sql, hidden, id);
    }


//    public int deleteById(long id) {
//        String sql = "DELETE FROM products WHERE id = ?";
//        return jdbcTemplate.update(sql, id); // Returns number of rows affected
//    }

    public Product findById(long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                (rs, rowNum) -> new Product(rs.getLong("id"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getString("description")
                ));
    }
}
