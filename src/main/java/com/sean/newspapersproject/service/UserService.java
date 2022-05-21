package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.MagazineRepository;
import com.sean.newspapersproject.repository.UserRepository;
import com.sean.newspapersproject.security.config.BcryptPasswordEncoder;
import com.sean.newspapersproject.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private ArticleService articleService;
    private CommentService commentService;
    private BcryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, ArticleService articleService,
                       CommentService commentService, BcryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.articleService = articleService;
        this.commentService = commentService;
        this.passwordEncoder = passwordEncoder;
    }



    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    public Set<Magazine> getAllFollowedMagazinesByUserId(Long id) {
        User user = getUserById(id);
        Set<Magazine> magazines = user.getFollowedMagazines();
        return magazines;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }

    public void save(User user) {
        String encodedPassword = passwordEncoder.passwordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void update(Long id, User updatedUser) {
        User user = userRepository.findById(id).get();
        if (updatedUser.getPassword().isEmpty() || updatedUser.getPassword().isBlank() || updatedUser.getPassword() == null) {
            updatedUser.setPassword(user.getPassword());
        } else {
            String encodedPassword = passwordEncoder.passwordEncoder().encode(updatedUser.getPassword());
            updatedUser.setPassword(encodedPassword);
        }
        updatedUser.setRole(user.getRole());
        userRepository.updateUserById(id, updatedUser);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        List<Article> articles = articleService.getAllArticlesByUserId(id);
        List<Comment> comments = commentService.getCommentsByUserId(id);
        for (Article article : articles) {
            articleService.delete(article);
        }

        for (Comment comment : comments) {
            commentService.delete(comment);
        }
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user;
    }
}
