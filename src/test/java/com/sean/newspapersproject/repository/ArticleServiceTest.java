package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.service.ArticleService;
import com.sean.newspapersproject.service.CategoryService;
import com.sean.newspapersproject.service.MagazineService;
import com.sean.newspapersproject.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MagazineService magazineService;

    @Test
    public void testSavingArticle() {
        User user = new User("username", "Bob", "Moskey", "username", "user@name.com");
        userService.save(user);
        Category category1 = new Category("Sport");
        categoryService.save(category1);
        Magazine magazine = new Magazine("Fashion Magazine", null);
        magazineService.save(magazine);
        Article article1 = new Article(
                "New Article",
                "Brief description of this article",
                "Some text",
                LocalDateTime.now(),
                user,
                category1,
                null,
                null
        );

        Article article2 = new Article(
                "New article2",
                "Brief description2 of this article",
                "Some text2",
                LocalDateTime.now().plusHours(3),
                null,
                category1,
                magazine,
                null
        );
        articleService.save(article1);
        articleService.save(article2);

        Article gotArticle1 = articleService.getArticleById(1L);
        Article gotArticle2 = articleService.getArticleById(2L);
        assertNotNull(gotArticle1);
        assertNotNull(gotArticle2);
        assertEquals("New Article", gotArticle1.getTitle());
        assertEquals("Brief description of this article", gotArticle1.getDescription());
        assertEquals("Some text", gotArticle1.getText());

        articleService.update(1L, article2);
        Article gotArticle3 = articleService.getArticleById(1L);
        assertNotNull(gotArticle3);
        assertEquals("New article2", gotArticle3.getTitle());
        assertEquals("Brief description2 of this article", gotArticle3.getDescription());
        assertEquals("Some text2", gotArticle3.getText());

    }

}