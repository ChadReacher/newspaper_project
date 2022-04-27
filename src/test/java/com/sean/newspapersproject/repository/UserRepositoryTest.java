package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.NewspapersProjectApplication;
import com.sean.newspapersproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSavingUser() {
        User user = new User("admin2", "admin2", "admin2@admin.com");
        userRepository.save(user);
        String username = "admin2";
//        User actualUser = userRepository.findById(1L).get();
        User actualUser = userRepository.findByUsername(username);
        assertEquals(actualUser.getUsername(), username);
    }

}