package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MagazineRepository magazineRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSavingArticle() {
        User user = new User("username", "username", "user@name.com");
        userRepository.save(user);
        Category category1 = new Category("Sport");
        categoryRepository.save(category1);
        Magazine magazine = new Magazine("Fashion Magazine");
        magazineRepository.save(magazine);
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
                category1,
                magazine
        );
        articleRepository.save(article1);
        articleRepository.save(article2);
    }

    @Test
    public void itShouldReturnArticle() {
        String expectedTitle = "New article2";
        Article article = articleRepository.findById(2L).get();
        assertEquals(expectedTitle, article.getTitle());
    }

}