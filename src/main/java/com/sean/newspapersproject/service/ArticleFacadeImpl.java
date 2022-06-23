package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ArticleFacadeImpl implements ArticleFacade {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public ArticleFacadeImpl(ArticleService articleService, CommentService commentService, LikeService likeService) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleService.getArticleById(id);

        List<Comment> comments = commentService.getCommentsByArticle(article);
        comments.forEach(commentService::delete);

        List<Like> likes = likeService.getLikesOfArticle(article);
        likes.forEach(likeService::delete);
        articleService.delete(article);
    }
}
