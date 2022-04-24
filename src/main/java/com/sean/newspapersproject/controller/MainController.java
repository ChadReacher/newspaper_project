package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.ArticleRepository;
import com.sean.newspapersproject.repository.CategoryRepository;
import com.sean.newspapersproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/")
public class MainController {

    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public MainController(ArticleRepository articleRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getMainPage(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("article/{id}")
    public String getArticlePage(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.getById(id);
        model.addAttribute("article", article);
        return "single_article";
    }

    @GetMapping("create-article")
    public String getArticleCreatePage(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "create_article";
    }


    @PostMapping("create-article")
    public String getArticleFromFormAndSave(@ModelAttribute("article") Article article) {
        User user = userRepository.getById(1L);
        Category category = categoryRepository.findByName(article.getCategory().getName());
        article.setCategory(category);
        article.setCreatedAt(LocalDateTime.now());
        article.setUserId(user);
        article.setMagazine(null);
        articleRepository.save(article);
        return "redirect:/";
    }

    @GetMapping("update-article/{id}")
    public String getUpdateArticleByID(@PathVariable("id") Long id, Model model) {
        Article articleToUpdate = articleRepository.getById(id);
        model.addAttribute("article", articleToUpdate);
        return "update_article";
    }

    @PostMapping("update-article/{id}")
    public String updateArticleByID(@PathVariable("id") Long id, @ModelAttribute("article") Article updatedArticle) {
        articleRepository.updateArticleById(id, updatedArticle);
        return "redirect:/";
    }


    @PostMapping("delete-article/{id}")
    public String deleteArticleById(@PathVariable("id") Long id) {
        articleRepository.deleteById(id);
        return "redirect:/";
    }
}
