package com.sean.newspapersproject.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    private Article article;

    @BeforeEach
    public void setup() {
        User user = new User("username", "password", "user@usr.com");
        Category category = new Category("Life");
        Magazine magazine = new Magazine("Fashion-profession");
        this.article = new Article("Interesting title", "Short description", "Big text", LocalDateTime.now(),
                user, category, null);
//        this.article = new Article("Interesting title", "Short description", "Big text", LocalDateTime.now(),
//                null, category, magazine);
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
            User user = new User("username", "password", "user@usr.com");
            assertEquals(user.getUsername(), article.getUserId().getUsername());
        }
    }

    @Test
    public void testCategoryFieldGetter() {
        Category category = new Category("Life");
        assertEquals(category.getName(), article.getCategory().getName());
    }

    @Test
    public void testMagazineFieldGetter() {
        if (article.getMagazine() != null) {
            Magazine magazine = new Magazine("Fashion-profession");
            assertEquals(magazine.getName(), article.getMagazine().getName());
        }
    }
}