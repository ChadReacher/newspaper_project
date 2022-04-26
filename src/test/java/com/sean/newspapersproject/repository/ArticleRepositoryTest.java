package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
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
    public void itShouldReturnArticle() {
        Article article = articleRepository.findById(2L).get();
        System.out.println(article);
//        when(articleRepository.getById(2L)).thenReturn(null);
    }
    @Test
    public void testSavingArticle() {
        User user = userRepository.findByUsername("username");
        Category category1 = categoryRepository.findByName("Sport");
        Magazine magazine = magazineRepository.findByName("Fashion Magazine");

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

}