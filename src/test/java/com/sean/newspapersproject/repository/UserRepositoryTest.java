package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSavingUser() {
        User user = new User("admin", "admin", "admin@admin.com");
        userRepository.save(user);
    }

    @Test
    public void testGettingUser() {
        String username = "admin";
        User actualUser = userRepository.findByUsername(username);
        assertEquals(actualUser.getUsername(), username);
    }
}