package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.service.ArticleService;
import com.sean.newspapersproject.service.CategoryService;
import com.sean.newspapersproject.service.CommentService;
import com.sean.newspapersproject.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testCommentGettingFromDB() {
        User user = userService.getUserByUsername("username");
        Category category = new Category("Sport");
        categoryService.save(category);
        Article article = new Article("Cool title", "cool desc", "cool text", LocalDateTime.now(), user, category, null, null);
        articleService.save(article);
        Comment expectedComment = new Comment(user, article, "Some message", LocalDateTime.now());
        commentService.save(expectedComment);
        Comment actualComment = commentService.getCommentsByUsername("username").stream().findFirst().orElse(null);
        assertEquals(expectedComment.getMessage(), actualComment.getMessage());
    }
}


