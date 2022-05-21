package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.service.ArticleService;
import com.sean.newspapersproject.service.CategoryService;
import com.sean.newspapersproject.service.MagazineService;
import com.sean.newspapersproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private MagazineService magazineService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

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
        articleService.delete((Long.valueOf(Integer.parseInt(id))));
        return "redirect:/admin/articles";
    }

    @GetMapping("/magazines")
    public String getAllMagazinesAdminPage(Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("magazines", magazineService.getAllMagazines());
        return "admin/admin_page_magazines";
    }

    @PostMapping("/magazines")
    public String createMagazineAdminPage(@ModelAttribute("magazine") Magazine magazine) {
        magazineService.save(magazine);
        return "redirect:/admin/magazines";
    }

    @GetMapping("/users")
    public String getAllUserAdminPage(Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin_page_users";
    }

    @PostMapping("/users")
    public String deleteUserByIdAdmin(@RequestParam("userId") String id) {
        userService.delete((long) Integer.parseInt(id));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/change-role")
    public String updateUserRoleById(@RequestParam("userId") String userId, @RequestParam("role") String role,
                                     @ModelAttribute("user") User updatedUser) {
        User user = userService.getUserById(Long.valueOf(userId));
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
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
    public String saveCategoryAdmin(@PathVariable("id") Long id, @ModelAttribute("category") Category updateCategory) {
        categoryService.update(id, updateCategory);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategoryAdmin(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }
}
