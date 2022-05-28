package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.configs.ImageAndModelSettings;
import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class MainController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final MagazineService magazineService;
    private final ImageService imageService;

    @Autowired
    public MainController(ArticleService articleService,
                          CategoryService categoryService,
                          MagazineService magazineService,
                          ImageService imageService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.magazineService = magazineService;
        this.imageService = imageService;
    }

    @GetMapping
    public String getMainPage(Model model) {
        List<Article> articles = articleService.getAllArticles().stream().skip(1).limit(5).collect(Collectors.toList());
        Article firstArticle = articleService.getAllArticles().get(0);
        model.addAttribute("firstArticle", firstArticle);
        initializeMapOfArticleImagesAndSetToModel(articles, model);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        String dateTimeFormatterPattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("articles")
    public String getAllArticlesPage(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        String dateTimeFormatterPattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        initializeMapOfArticleImagesAndSetToModel(articles, model);
        return "articles";
    }

    @GetMapping("magazines")
    public String getAllMagazinesPage(Model model) {
        List<Magazine> magazines = magazineService.getAllMagazines();
        model.addAttribute("magazines", magazines);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        initializeMapOfMagazineImagesAndSetToModel(magazines, model);
        return "magazines";
    }

    public void initializeMapOfArticleImagesAndSetToModel(List<Article> articles, Model model) {
        Map<Article, String> mapOfArticleAndItsImage = new HashMap<>();
        for (Article article : articles) {
            try {
                String imageString = Base64.getMimeEncoder().encodeToString(article.getImageId().getImageData());
                mapOfArticleAndItsImage.put(article, imageString);
            } catch (Exception e) {
                String imageString = "";
                mapOfArticleAndItsImage.put(article, imageString);
            }
        }
        model.addAttribute("mapOfArticleAndItsImage", mapOfArticleAndItsImage);
    }

    public void initializeMapOfMagazineImagesAndSetToModel(List<Magazine> magazines, Model model) {
        Map<Magazine, String> mapOfMagazineAndItsImage = new HashMap<>();
        for (Magazine magazine : magazines) {
            try {
                String imageString = Base64.getMimeEncoder().encodeToString(magazine.getImageId().getImageData());
                mapOfMagazineAndItsImage.put(magazine, imageString);
            } catch (Exception e) {
                String imageString = "";
                mapOfMagazineAndItsImage.put(magazine, imageString);
            }
        }
        model.addAttribute("mapOfMagazineAndItsImage", mapOfMagazineAndItsImage);
    }

}
