package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {

    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    CommentRepositoryTest(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void testCommentGettingFromDB() {
        User user = userRepository.findByUsername("username");
        Comment expectedComment = new Comment(user, "Some message", LocalDateTime.now());
        commentRepository.save(expectedComment);
        Comment actualComment = commentRepository.findByUserId(user).stream().findFirst().orElse(null);
        assertEquals(expectedComment.getMessage(), actualComment.getMessage());
        assertEquals(expectedComment.getCreatedAt(), actualComment.getCreatedAt());
    }
}


