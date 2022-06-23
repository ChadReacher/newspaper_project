package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Like;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LikeService {

    private LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public Like getLikeById(Long id) {
        return likeRepository.findById(id).orElse(null);
    }

    public boolean isUserLikedTheArticle(User user, Article article) {
        if (article.getUserId().equals(user)) {
            return true;
        }
        List<Like> allLikesOfArticle = likeRepository.findAllByArticleId(article);
        for (Like like : allLikesOfArticle) {
            if (like.getUserId().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public List<Like> getLikesOfUser(User user) {
        return likeRepository.findAllByUserId(user);
    }

    public List<Like> getLikesOfArticle(Article article) {
        return likeRepository.findAllByArticleId(article);
    }


    public int getNumberOfLikesOfArticle(Article article) {
        return likeRepository.findAllByArticleId(article).size();
    }

    public void save(Like like) {
        likeRepository.save(like);
    }

    @Transactional
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }

    public void delete(Like like) {
        likeRepository.delete(like);
    }

    public Like getLikeByUserAndArticle(User user, Article article) {
        List<Like> allLikesOfArticle = likeRepository.findAllByArticleId(article);
        return allLikesOfArticle.stream()
                .filter(like -> like.getUserId().equals(user) && like.getArticleId().equals(article))
                .findFirst().orElseThrow(() -> new RuntimeException("Cannot find like by the user and article."));
    }
}
