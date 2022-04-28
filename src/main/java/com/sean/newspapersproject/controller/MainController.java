package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.ArticleRepository;
import com.sean.newspapersproject.repository.CategoryRepository;
import com.sean.newspapersproject.repository.UserRepository;
import com.sean.newspapersproject.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/")
public class MainController {

    private ArticleService articleService;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public MainController(ArticleService articleService, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.articleService = articleService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getMainPage(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("article/{id}")
    public String getArticlePage(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "single_article";
    }

    @GetMapping("create-article")
    public String getArticleCreatePage(Model model) {
        List<Category> categories = categoryRepository.findAll(); // TODO: change categoryRepository to new CategoryService
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "create_article";
    }


    @PostMapping("create-article")
    public String getArticleFromFormAndSave(@ModelAttribute("article") Article article) {
        User user = userRepository.findById(1L).get();
        articleService.saveArticleWithUser(article, user);
        return "redirect:/";
    }

    @GetMapping("update-article/{id}")
    public String getUpdateArticleByID(@PathVariable("id") Long id, Model model) {
        Article articleToUpdate = articleService.getArticleById(id);
        model.addAttribute("article", articleToUpdate);
        return "update_article";
    }

    @PostMapping("update-article/{id}")
    public String updateArticleByID(@PathVariable("id") Long id, @ModelAttribute("article") Article updatedArticle) {
        articleService.update(id, updatedArticle);
        return "redirect:/";
    }


    @PostMapping("delete-article/{id}")
    public String deleteArticleById(@PathVariable("id") Long id) {
        articleService.delete(id);
        return "redirect:/";
    }
}
