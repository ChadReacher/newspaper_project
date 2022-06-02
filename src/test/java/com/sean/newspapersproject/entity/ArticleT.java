package com.sean.newspapersproject.entity;

import com.sean.newspapersproject.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleT {

    private Article article;

    @BeforeEach
    public void setup() {
        User user = new User("username", "Bob", "Marley", "password", "user@usr.com", null, Role.USER);
        Category category = new Category("Life");
        Magazine magazine = new Magazine("Fashion-profession", null, user);
        this.article = new Article("Interesting title", "Short description", "Big text", LocalDateTime.now(),
                magazine.getAuthor(), category, magazine, null);
    }


    @Test
    public void testTitleFieldGetter() {
        String title = "Interesting title";
        assertEquals(title, article.getTitle());
    }

    @Test
    public void testDescriptionFieldGetter() {
        String description = "Short description";
        assertEquals(description, article.getDescription());
    }

    @Test
    public void testTextFieldGetter() {
        String text = "Big text";
        assertEquals(text, article.getText());
    }

    @Test
    public void testUserFieldGetter() {
        if (article.getUserId() != null) {
            User user = new User("username", "Lada", "Sedan", "password", "user@usr.com", null, null);
            assertEquals(user.getUsername(), article.getUserId().getUsername());
        }
    }

    @Test
    public void testCategoryFieldGetter() {
        Category category = new Category("Life");
        assertEquals(category.getName(), article.getCategory().getName());
    }

}