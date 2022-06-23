package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private CategoryService categoryService;
    private LikeService likeService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CategoryService categoryService, LikeService likeService) {
        this.articleRepository = articleRepository;
        this.categoryService = categoryService;
        this.likeService = likeService;
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

    public List<Article> getArticlesPages(Integer pageNumber, String sortColumn) {
        Pageable firstPageWithTenArticlesSortedByName =
                PageRequest.of(pageNumber - 1, 10, Sort.by(sortColumn).ascending());
        return articleRepository.findAll(firstPageWithTenArticlesSortedByName).toList();
    }

    public void saveArticle(Article article) {
        Category category = categoryService.getCategoryByName(article.getCategory().getName());
        article.setCategory(category);
        article.setCreatedAt(LocalDateTime.now());
        articleRepository.save(article);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public void update(Long id, Article updatedArticle) {
        Article articleToUpdate = getArticleById(id);
        articleRepository.updateArticleById(id, updatedArticle);
        articleToUpdate.setImageId(updatedArticle.getImageId());
    }


    @Transactional
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public List<Article> getAllArticleByMagazine(Magazine magazine) {
        return articleRepository.findAllByMagazine(magazine);
    }
}
