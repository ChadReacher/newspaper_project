package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.configs.ImageAndModelSettings;
import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.security.SecurityUser;
import com.sean.newspapersproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ArticleController {

    private final ArticleService articleService;
    private final MagazineService magazineService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public ArticleController(ArticleService articleService, MagazineService magazineService, CategoryService categoryService,
                             ImageService imageService, CommentService commentService, LikeService likeService) {
        this.articleService = articleService;
        this.magazineService = magazineService;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    public User getAuthenticatedUserFromPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        return user;
    }

    public void setFormattedDateToTheModelAtArticlePage(LocalDateTime initialDate, Model model) {
        String dateTimeFormatterPattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
        String formatDateTime = initialDate.format(dateTimeFormatter);
        model.addAttribute("articleCreatedAt", formatDateTime);
    }

    @GetMapping("article/{id}")
    public String getArticleByIdPage(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        List<Comment> articleComments = commentService.getCommentsByArticle(article);

        model.addAttribute("article", article);
        model.addAttribute("comments", articleComments);

        String dateTimeFormatterPattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);

        int numberOfLikes = likeService.getNumberOfLikesOfArticle(article);
        model.addAttribute("numberOfLikes", numberOfLikes);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = getAuthenticatedUserFromPage();
            boolean isUserAnAuthorOfArticle = article.getUserId().equals(user);
            boolean isUserLikedTheArticle = likeService.isUserLikedTheArticle(user, article);
            model.addAttribute("isUserAnAuthorOfArticle", isUserAnAuthorOfArticle);
            model.addAttribute("isUserLikedTheArticle", isUserLikedTheArticle);
        }

        initializeMapOfUserImagesInCommentsAndSetToModel(articleComments, model);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        ImageAndModelSettings.getImageStringFromArticleAndPutInModel(article, model);

        return "article/single_article";
    }

    public void initializeMapOfUserImagesInCommentsAndSetToModel(List<Comment> comments, Model model) {
        Map<Comment, String> mapOfUsersImagesInComments = new HashMap<>();
        for (Comment comment : comments) {
            String imageString = Base64.getMimeEncoder().encodeToString(comment.getUserId().getImageId().getImageData());
            mapOfUsersImagesInComments.put(comment, imageString);
        }
        model.addAttribute("mapOfUsersImagesInComments", mapOfUsersImagesInComments);
    }

    @PostMapping("article/{id}/comment")
    public String setCommentToTheArticle(@PathVariable("id") Long id, @ModelAttribute("comment") Comment createdComment) {
        Article article = articleService.getArticleById(id);
        commentService.postCommentToTheArticle(createdComment, article);
        return "redirect:/article/" + article.getArticleId();
    }

    @PostMapping("article/{id}/like")
    public String setLikeToArticleById(@PathVariable("id") Long id) {
        Article article = articleService.getArticleById(id);
        User user = getAuthenticatedUserFromPage();
        boolean isUserLikedTheArticle = likeService.isUserLikedTheArticle(user, article);
        if (isUserLikedTheArticle) {
            Like like = likeService.getLikeByUserAndArticle(user, article);
            likeService.delete(like);
        } else {
            Like articleLike = new Like(user, article);
            likeService.save(articleLike);
        }
        return "redirect:/article/" + article.getArticleId();
    }

    @GetMapping("create-article")
    public String getArticleCreationPage(Model model) {
        User user = getAuthenticatedUserFromPage();
        Magazine magazine = magazineService.getMagazineByAuthor(user);
        if (magazine == null) {
            return "redirect:/";
        }
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "article/create_article";
    }


    @PostMapping("create-article")
    public String saveArticleFromForm(@ModelAttribute("article") Article createdArticle,
                                      @RequestParam("image") MultipartFile imageData) {
        saveImageAndSetToArticle(imageData, createdArticle);
        User user = getAuthenticatedUserFromPage();
        articleService.saveArticleWithUser(createdArticle, user);
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getUpdateArticleByIdPage(@PathVariable("id") Long id, Model model) {
        Article articleToUpdate = articleService.getArticleById(id);
        User user = getAuthenticatedUserFromPage();
        Magazine magazine = magazineService.getMagazineByAuthor(user);
        if (!articleService.getAllArticleByMagazine(magazine).contains(articleToUpdate)) {
            return "redirect:/";
        }
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("article", articleToUpdate);
        return "article/update_article";
    }

    @PostMapping("update-article/{id}")
    public String updateArticleById(@PathVariable("id") Long id, @ModelAttribute("article") Article updatedArticle,
                                    @RequestParam("imageData") MultipartFile imageData) {
        saveImageAndSetToArticle(imageData, updatedArticle);
        articleService.update(id, updatedArticle);
        return "redirect:/";
    }

    @PostMapping("delete-article/{id}")
    public String deleteArticleById(@PathVariable("id") Long id) {
        articleService.delete(id);
        return "redirect:/";
    }
}
