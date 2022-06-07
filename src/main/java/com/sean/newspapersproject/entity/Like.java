package com.sean.newspapersproject.entity;

import javax.persistence.*;

@Entity
@Table(name = "article_likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "article_id")
    private Article articleId;

    public Like(User userId, Article articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    public Like() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Article getArticleId() {
        return articleId;
    }

    public void setArticleId(Article articleId) {
        this.articleId = articleId;
    }
}
