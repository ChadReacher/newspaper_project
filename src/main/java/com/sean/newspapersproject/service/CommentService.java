package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.CategoryRepository;
import com.sean.newspapersproject.repository.CommentRepository;
import com.sean.newspapersproject.repository.UserRepository;
import com.sean.newspapersproject.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleService articleService;


    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments;
    }

    public Comment getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        return comment;
    }

    public List<Comment> getCommentsByUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        List<Comment> comments = commentRepository.findByUserId(user);
        return comments;
    }

    public List<Comment> getCommentsByArticle(Article article) {
        List<Comment> comments = commentRepository.findByArticleId(article);
        return comments;
    }

    public List<Comment> getCommentsByUserId(Long id) {
        User user = userRepository.getById(id);
        List<Comment> comments = commentRepository.findByUserId(user);
        return comments;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void update(Long id, Comment updatedComment) {
        commentRepository.updateCommentById(id, updatedComment);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public void postCommentToTheArticle(Comment createdComment, Article article) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = ((SecurityUser) authentication.getPrincipal()).getUser();
        createdComment.setArticleId(article);
        createdComment.setUserId(authenticatedUser);
        createdComment.setCreatedAt(LocalDateTime.now());
        save(createdComment);
    }
}
