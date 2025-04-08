package com.demoProj.demoProject.services;

import com.demoProj.demoProject.models.User;
import com.demoProj.demoProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void register(User user) {
        userRepository.addUser(user);
    }

    public boolean userExists(String username) {
        return userRepository.findAllUsernames().contains(username);
    }

    public boolean correctPassword(String username, String password) {
        if(userExists(username)) {
            if(userRepository.findByUsername(username).getPassword().equals(password)){
                return true;
            }
        }else{
            return false;
        }
        return false;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }


    public int updateUser(Long userId, String username, String password, String role) {
        return userRepository.updateUser(userId, username, password, role);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId);
    }



}
