package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private CategoryService categoryService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CategoryService categoryService) {
        this.articleRepository = articleRepository;
        this.categoryService = categoryService;
    }

    public List<Article> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles;
    }

    public Article getArticleById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        return article;
    }

    public List<Article> getArticlesPages(Integer pageNumber) {
        Pageable firstPageWithTenArticles = PageRequest.of(pageNumber - 1, 10);
        List<Article> articles = articleRepository.findAll(firstPageWithTenArticles).toList();
        return articles;
    }

    public List<Article> getAllArticlesByUserId(Long id) {
        List<Article> articles = articleRepository.findAllByUserId(id);
        return articles;
    }

    public void saveArticleWithUser(Article article, User user) {
        Category category = categoryService.getCategoryByName(article.getCategory().getName());
        article.setCategory(category);
        article.setCreatedAt(LocalDateTime.now());
        if (user != null) {
            article.setUserId(user);
            article.setMagazine(null);
        } else {
            article.setUserId(null);
            article.setMagazine(null);
        }
        articleRepository.save(article);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    @Transactional
    public void update(Long id, Article updatedArticle) {
        Article articleToUpdate = getArticleById(id);
        articleRepository.updateArticleById(id, updatedArticle);
        articleRepository.save(articleToUpdate);
    }

    @Transactional
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public void delete(Article article) {
        articleRepository.delete(article);
    }

}
