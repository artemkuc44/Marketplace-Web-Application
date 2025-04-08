package com.demoProj.demoProject.repositories;

import com.demoProj.demoProject.models.Role;
import com.demoProj.demoProject.models.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "\role VARCHAR(255) NOT NULL"
                + ")";
        jdbcTemplate.execute(sql);
    }


    public int addUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole().toString());//update used to add/delete data
    }

    public List<String> findAllUsernames() {
        String sql = "SELECT username FROM users";
        return jdbcTemplate.queryForList(sql, String.class);//queryForList fetches single column
    }

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("id"),
                rs.getString("username"), rs.getString("password"),Role.valueOf(rs.getString("role"))));
    }



    // Find user by username
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username},//queryForObject fetches one record
                    (rs, rowNum) -> new User(rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role"))));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n findByUsername returned null");
            return null; // Returns null if no user is found
        }
    }

    public int updateUser(Long id, String username, String password, String role) {

        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        return jdbcTemplate.update(sql, username, password, role, id.toString());
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new User(rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))));
    }

}




