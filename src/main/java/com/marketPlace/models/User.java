package com.demoProj.demoProject.models;



public class User {
    private Long id;
    private String username;
    private String password;
    private Role role;
    // Constructors
    public User() {
        this.role = Role.USER;
    }


    public User(Long id, String username, String password, Role role) {
        this.id = id;//id managed in db, when user found by username new instance is created with id from db
        this.username = username;
        this.password = password;
        this.role = role;
    }


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

}
