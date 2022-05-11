package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSavingUser() {
        User user = new User("admin2", "Admin", "Adminovish", "admin2", "admin2@admin.com", null, null);
        userService.save(user);
        String username = "admin2";
        User actualUser = userService.getUserByUsername(username);
        assertEquals(actualUser.getUsername(), username);
    }

    @Test
    public void testUpdatingUser() {
        User user = new User("admin3","Admin", "Adminovish", "admin3", "admin3@admin.com", null, null);
        userService.save(user);
        String username = "admin3";
        User actualUser = userService.getUserByUsername(username);
        User updatedUser = new User("admin","Admin", "Adminovish", "admin", "admin@workout.com", null, null);
        userService.update(1L, updatedUser);
        actualUser = userService.getUserByUsername("admin");
        assertEquals("admin", actualUser.getUsername());
        assertEquals("admin", actualUser.getPassword());
        assertEquals("admin@workout.com", actualUser.getEmail());
    }

}