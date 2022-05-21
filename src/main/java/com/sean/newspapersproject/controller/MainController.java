package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.security.SecurityUser;
import com.sean.newspapersproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
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
        List<Article> articles = articleService.getAllArticles().stream().limit(5).collect(Collectors.toList());
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("articles", articles);
        return "main";
    }

    @GetMapping("article/{id}")
    public String getArticleByIdPage(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        ImageAndModelSettings.getImageStringFromArticleAndPutInModel(article, model);
        model.addAttribute("article", article);
        return "single_article";
    }

    @GetMapping("create-article")
    public String getArticleCreationPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        Magazine magazine = magazineService.getMagazineByAuthor(user);
        if (magazine == null) {
            return "redirect:/";
        }
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "create_article";
    }


    @PostMapping("create-article")
    public String saveArticleWithForm(@ModelAttribute("article") Article updatedArticle,
                                            @RequestParam("image") MultipartFile imageData) {
        saveImageAndSetToArticle(imageData, updatedArticle);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        articleService.saveArticleWithUser(updatedArticle, user);
        return "redirect:/";
    }

    public void saveImageAndSetToArticle(MultipartFile imageData, Article article) {
        try {
            String fileName = StringUtils.cleanPath(imageData.getOriginalFilename());
            if (fileName.isEmpty()) {
                article.setImageId(null);
            } else {
                Image image = new Image(fileName, imageData.getBytes());
                imageService.save(image);
                article.setImageId(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("update-article/{id}")
    public String getUpdateArticleByIdPage(@PathVariable("id") Long id, Model model) {
        Article articleToUpdate = articleService.getArticleById(id);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("article", articleToUpdate);
        return "update_article";
    }

    @PostMapping("update-article/{id}")
    public String updateArticleById(@PathVariable("id") Long id, @ModelAttribute("article") Article updatedArticle) {
        articleService.update(id, updatedArticle);
        return "redirect:/";
    }


    @PostMapping("delete-article/{id}")
    public String deleteArticleById(@PathVariable("id") Long id) {
        articleService.delete(id);
        return "redirect:/";
    }


    @GetMapping("magazine/{id}")
    public String getMagazineByIdPage(@PathVariable("id") Long id, Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        Magazine magazine = magazineService.getMagazineById(id);
        model.addAttribute("magazine", magazine);
        return "magazine/single_magazine";
    }

    @GetMapping("create-magazine")
    public String getMagazineCreationPage(Model model) {
        model.addAttribute("magazine", new Magazine());
        return "magazine/create_magazine";
    }

    @PostMapping("create-magazine")
    public String createMagazine(@ModelAttribute("magazine") Magazine magazine, @RequestParam("imageData") MultipartFile imageData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        magazine.setAuthor(user);
        saveImageAndSetToMagazine(imageData, magazine);
        magazineService.save(magazine);
        return "redirect:/magazine";
    }

    public void saveImageAndSetToMagazine(MultipartFile imageData, Magazine magazine) {
        try {
            String fileName = StringUtils.cleanPath(imageData.getOriginalFilename());
            if (fileName.isEmpty()) {
                magazine.setImageId(null);
            } else {
                Image image = new Image(fileName, imageData.getBytes());
                imageService.save(image);
                magazine.setImageId(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("magazine-update/{id}")
    public String getUpdateMagazinePage(@PathVariable("id") Long id, Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        Magazine magazineToUpdate = magazineService.getMagazineById(id);
        model.addAttribute("magazine", magazineToUpdate);
        return "magazine/edit_magazine";
    }

    @PostMapping("magazine-update/{id}")
    public String updateMagazine(@PathVariable("id") Long id, @ModelAttribute("magazine") Magazine updatedMagazine) {
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

    @GetMapping("magazine")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getMagazinePageFromAuthenticatedUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        Magazine magazine = magazineService.getMagazineByAuthor(user);
        if (magazine == null) {
            return "redirect:/";
        }
        try {
            String imageString = Base64.getMimeEncoder().encodeToString(magazine.getImageId().getImageData());
            model.addAttribute("magazineImage", imageString);
        } catch (Exception e) {
            String imageString = "";
            model.addAttribute("magazineImage", imageString);
        }
        model.addAttribute("magazine", magazine);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        List<Article> articlesFromMagazine = articleService.getAllArticleByMagazine(magazine);
        model.addAttribute("articlesFromMagazine", articlesFromMagazine);
        return "magazine/magazine_managing";
    }

    @PostMapping("magazine/update")
    public String updateAuthenticatedUsersMagazine(@ModelAttribute("magazine") Magazine updatedMagazine,
                                                   @RequestParam("imageData") MultipartFile imageData) {
        if (imageData != null) {
            saveImageAndSetToMagazine(imageData, updatedMagazine);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        Magazine magazine = magazineService.getMagazineByAuthor(user);
        magazineService.update(magazine.getMagazineId(), updatedMagazine);
        return "redirect:/magazine";
    }
}
