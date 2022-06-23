package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.configs.ImageAndModelSettings;
import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.security.Role;
import com.sean.newspapersproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private ArticleService articleService;
    private MagazineService magazineService;
    private UserService userService;
    private CategoryService categoryService;
    private ImageService imageService;
    private ArticleFacade articleFacade;
    private UserFacade userFacade;

    @Autowired
    public AdminController(ArticleService articleService, MagazineService magazineService, UserService userService,
                           CategoryService categoryService, ImageService imageService, ArticleFacade articleFacade,
                           UserFacade userFacade) {
        this.articleService = articleService;
        this.magazineService = magazineService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.articleFacade = articleFacade;
        this.userFacade = userFacade;
    }

    @GetMapping
    public String getDashboardPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        return "admin/admin_page";
    }

    @GetMapping("/articles")
    public String getAllArticlesAdminPage(@RequestParam(required = false, name = "page", defaultValue = "1") String pN,
                                          @RequestParam(required = false, name = "column") String sortColumn,
                                          Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        Integer pageNumber = Integer.valueOf(pN);
        List<Article> allArticles = articleService.getArticlesPages(pageNumber);
        if (sortColumn != null) {
            allArticles = articleService.getArticlesPages(pageNumber, sortColumn);
        }
        model.addAttribute("articles", allArticles);
        model.addAttribute("currentPageNumber", pageNumber);
        model.addAttribute("quantityOfPages", articleService.getAllArticles().size() / 10);
        return "admin/admin_page_articles";
    }


    @PostMapping("/articles")
    public String deleteArticleByIdAdmin(@RequestParam("articleId") String id) {
        articleFacade.deleteArticle(Long.valueOf(id));
        return "redirect:/admin/articles";
    }

    @GetMapping("/magazines")
    public String getAllMagazinesAdminPage(Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("magazines", magazineService.getAllMagazines());
        Map<Magazine, Integer> numberOfPublishedArticlesByMagazine = new HashMap<>();
        for (Magazine m : magazineService.getAllMagazines()) {
            List<Article> articles = articleService.getAllArticleByMagazine(m);
            numberOfPublishedArticlesByMagazine.put(m, articles.size());
        }
        model.addAttribute("numberOfPublishedArticlesByMagazine", numberOfPublishedArticlesByMagazine);
        return "admin/admin_page_magazines";
    }

    @PostMapping("/magazines")
    public String createMagazineAdmin(@ModelAttribute("magazine") Magazine magazine) {
        magazineService.save(magazine);
        return "redirect:/admin/magazines";
    }

    @GetMapping("/users")
    public String getAllUserAdminPage(Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("users", userService.getAllUsers());
        Map<User, Magazine> mapOfUsersAndTheirMagazine = new HashMap<>();
        for (User user : userService.getAllUsers()) {
            Magazine magazine = magazineService.getMagazineByAuthor(user);
            mapOfUsersAndTheirMagazine.put(user, magazine);
        }
        model.addAttribute("mapOfUsersAndTheirMagazine", mapOfUsersAndTheirMagazine);
        return "admin/admin_page_users";
    }

    @PostMapping("/users")
    public String deleteUserByIdAdmin(@RequestParam("userId") String id) {
        userFacade.deleteUser(Long.valueOf(id));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/change-role")
    public String updateUserRoleById(@RequestParam("userId") String userId, @RequestParam("role") String role,
                                     @ModelAttribute("user") User updatedUser) {
        updatedUser.setRole(Role.valueOf(role));
        userService.update(Long.valueOf(userId), updatedUser);
        return "redirect:/admin/users";
    }


    @GetMapping("/categories")
    public String getAllCategoriesAdminPage(Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());
        Map<String, Integer> numberOfArticlesAccordingToCategory = getMapOfArticlesAndTheirViews();
        model.addAttribute("map", numberOfArticlesAccordingToCategory);
        return "admin/admin_page_categories";
    }

    public Map<String, Integer> getMapOfArticlesAndTheirViews() {
        Map<String, Integer> numberOfArticlesAccordingToCategory = new HashMap<>();
        for (Article article : articleService.getAllArticles()) {
            String categoryName = article.getCategory().getName();
            if (numberOfArticlesAccordingToCategory.containsKey(categoryName)) {
                numberOfArticlesAccordingToCategory.put(categoryName,
                        numberOfArticlesAccordingToCategory.get(categoryName) + 1);
            } else {
                numberOfArticlesAccordingToCategory.put(categoryName, 1);
            }
        }
        return numberOfArticlesAccordingToCategory;
    }

    @PostMapping("/categories")
    public String saveCategoryAdmin(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/edit/{id}")
    public String updateCategoryAdmin(@PathVariable("id") Long id, @ModelAttribute("category") Category updateCategory) {
        categoryService.update(id, updateCategory);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategoryAdmin(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }

    @PostMapping("magazine-update/{id}")
    public String updateMagazine(@PathVariable("id") Long id, @ModelAttribute("magazine") Magazine updatedMagazine,
                                 @RequestParam("imageData") MultipartFile imageData) {
        saveImageAndSetToMagazine(imageData, updatedMagazine);
        magazineService.update(id, updatedMagazine);
        return "redirect:/";
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
}
