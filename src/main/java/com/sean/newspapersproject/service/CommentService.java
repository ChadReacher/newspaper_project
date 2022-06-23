package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.CommentRepository;
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

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByUser(User user) {
        return commentRepository.findAllByUserId(user);
    }

    public List<Comment> getCommentsByArticle(Article article) {
        return commentRepository.findByArticleId(article);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void postCommentToTheArticle(Comment createdComment, Article article) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = ((SecurityUser) authentication.getPrincipal()).getUser();
        createdComment.setArticleId(article);
        createdComment.setUserId(authenticatedUser);
        createdComment.setCreatedAt(LocalDateTime.now());
        save(createdComment);
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
}
