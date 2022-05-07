package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class MainController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final MagazineService magazineService;
    private final ImageService imageService;

    @Autowired
    public MainController(ArticleService articleService,
                          CategoryService categoryService,
                          UserService userService,
                          MagazineService magazineService,
                          ImageService imageService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.magazineService = magazineService;
        this.imageService = imageService;
    }

    @GetMapping
    public String getMainPage(Model model) {
        List<Article> articles = articleService.getAllArticles().stream().limit(5).collect(Collectors.toList());
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("article/{id}")
    public String getArticlePage(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        String imageString = Base64.getMimeEncoder().encodeToString(article.getImageId().getImageData());
        model.addAttribute("imageString", imageString);
        model.addAttribute("article", article);
        return "single_article";
    }

    @GetMapping("create-article")
    public String getArticleCreatePage(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "create_article";
    }


    @PostMapping("create-article")
    public String getArticleFromFormAndSave(@ModelAttribute("article") Article article, @RequestParam("image") MultipartFile imageData) {
        try {
            String fileName = StringUtils.cleanPath(imageData.getOriginalFilename());
            Image image = new Image(fileName, imageData.getBytes());
            imageService.save(image);
            article.setImageId(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = userService.getUserById(1L);
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

    @GetMapping("magazine/{id}")
    public String getMagazinePageById(@PathVariable("id") Long id, Model model) {
        Magazine magazine = magazineService.getMagazineById(id);
        model.addAttribute("magazine", magazine);
        return "magazine/single_magazine";
    }

    @GetMapping("create-magazine")
    public String getCreateMagazinePage(Model model) {
        model.addAttribute("magazine", new Magazine());
        return "magazine/create_magazine";
    }

    @PostMapping("create-magazine")
    public String createMagazine(@ModelAttribute("magazine") Magazine magazine) {
        magazineService.save(magazine);
        return "redirect:/";
    }

    @GetMapping("magazine-update/{id}")
    public String getUpdateMagazinePage(@PathVariable("id") Long id, Model model) {
        Magazine magazineToUpdate = magazineService.getMagazineById(id);
        model.addAttribute("magazine", magazineToUpdate);
        return "magazine/edit_magazine";
    }

    @PostMapping("magazine-update/{id}")
    public String uppdateMagazinePage(@PathVariable("id") Long id, @ModelAttribute("magazine") Magazine updatedMagazine) {
        Magazine magazineToUpdate = magazineService.getMagazineById(id);
        magazineToUpdate.setName(updatedMagazine.getName());
        magazineService.save(magazineToUpdate);
        return "redirect:/";
    }

    @PostMapping("delete-magazine/{id}")
    public String deleteMagazineById(@PathVariable("id") Long id) {
        magazineService.delete(id);
        return "redirect:/";
    }
}
