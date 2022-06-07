package com.sean.newspapersproject.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Article {

    @Id
    @SequenceGenerator(
            name = "article_sequence",
            sequenceName = "article_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_sequence")
    private Long articleId;
    private String title;
    private String description;
    private String text;
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image imageId;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "magazine_id")
    private Magazine magazine;

    public Article(String title, String description, String text, LocalDateTime createdAt, User userId, Category category, Magazine magazine,
                   Image imageId) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
        this.category = category;
        this.magazine = magazine;
        this.imageId = imageId;
    }

    public Article() {

    }

    public Image getImageId() {
        return imageId;
    }

    public void setImageId(Image imageId) {
        this.imageId = imageId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }


    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                ", category=" + category +
                ", magazine=" + magazine +
                '}';
    }
}
