package com.example.backend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    public User updateUser(Long id, User userDetails){
        User user = getUser(id);
        if(user != null){
            user.setFull_name(userDetails.getFull_name());
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            return userRepository.save(user);
        }
        return null;
    }
}
