package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.CategoryRepository;
import com.sean.newspapersproject.repository.CommentRepository;
import com.sean.newspapersproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

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
        User user = userRepository.findByUsername(username);
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
}
