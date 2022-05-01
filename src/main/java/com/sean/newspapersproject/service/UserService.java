package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.ArticleRepository;
import com.sean.newspapersproject.repository.MagazineRepository;
import com.sean.newspapersproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private MagazineRepository magazineRepository;
    private ArticleService articleService;
    private CommentService commentService;

    @Autowired
    public UserService(UserRepository userRepository, MagazineRepository magazineRepository, ArticleService articleService, CommentService commentService) {
        this.userRepository = userRepository;
        this.magazineRepository = magazineRepository;
        this.articleService = articleService;
        this.commentService = commentService;
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
        User user = userRepository.findByUsername(username);
        return user;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void update(Long id, User updatedUser) {
        userRepository.updateUserById(id, updatedUser);
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
}
