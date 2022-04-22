package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void saveArticle() {
        User user = new User("admin", "admin", "admin@admin.com");
        Category category1 = new Category("Sport");
        Category category2 = new Category("Life");
        Magazine magazine = new Magazine("Fashion Magazine");

        Article article1 = new Article(
                "New article",
                "Brief description of this article",
                "Some text",
                LocalDateTime.now(),
                user,
                category1,
                null
        );

        Article article2 = new Article(
                "New article2",
                "Brief description2 of this article",
                "Some text2",
                LocalDateTime.now().plusHours(3),
                null,
                category2,
                magazine
        );
        articleRepository.save(article1);
        articleRepository.save(article2);
    }
}