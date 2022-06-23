package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.repository.CommentRepository;
import com.sean.newspapersproject.repository.MagazineRepository;
import com.sean.newspapersproject.repository.UserRepository;
import com.sean.newspapersproject.security.config.BcryptPasswordEncoder;
import com.sean.newspapersproject.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private BcryptPasswordEncoder passwordEncoder;
    private MagazineService magazineService;

    @Autowired
    public UserService(UserRepository userRepository, BcryptPasswordEncoder passwordEncoder, MagazineService magazineService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.magazineService = magazineService;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    public Set<Magazine> getAllFollowedMagazinesByUserId(Long id) {
        User user = getUserById(id);
        Set<Magazine> magazines = user.getFollowedMagazines();
        return magazines;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }

    public void save(User user) {
        if (!user.getPassword().startsWith("$2a$10$")) {
            String encodedPassword = passwordEncoder.passwordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        userRepository.save(user);
    }


    public void followMagazine(User user, Magazine magazine) {
        user.getFollowedMagazines().add(magazine);
        magazine.getFollowedUsers().add(user);
        userRepository.save(user);
        magazineService.save(magazine);

    }

    public void unfollowMagazine(User user, Magazine magazine) {
        user.getFollowedMagazines().remove(magazine);
        magazine.getFollowedUsers().remove(user);
        userRepository.save(user);
        magazineService.save(magazine);
    }

    @Transactional
    public void update(Long id, User updatedUser) {
        User userToUpdate = getUserById(id);
        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(userToUpdate.getEmail());
        }
        if (updatedUser.getRole() == null) {
            updatedUser.setRole(userToUpdate.getRole());
        }
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty() || updatedUser.getPassword().isBlank()) {
            updatedUser.setPassword(userToUpdate.getPassword());
        } else {
            String encodedPassword = passwordEncoder.passwordEncoder().encode(updatedUser.getPassword());
            updatedUser.setPassword(encodedPassword);
        }
        userRepository.updateUserById(id, updatedUser);
    }

    @Transactional
    protected void delete(User user) {
        userRepository.delete(user);
    }

    public boolean isUserWithUsernameExists(String username) {
        return userRepository.findByUsername(username).orElse(null) != null;
    }

    public boolean isUserWithEmailExists(String email) {
        return userRepository.findByEmail(email).orElse(null) != null;
    }

}
