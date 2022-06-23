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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Controller
@RequestMapping("/")
public class MagazineController {

    private final MagazineService magazineService;
    private final ImageService imageService;
    private final ArticleService articleService;
    private final UserService userService;
    private final MagazineFacade magazineFacade;

    @Autowired
    public MagazineController(MagazineService magazineService, ImageService imageService, ArticleService articleService,
                              UserService userService, MagazineFacade magazineFacade) {
        this.magazineService = magazineService;
        this.imageService = imageService;
        this.articleService = articleService;
        this.userService = userService;
        this.magazineFacade = magazineFacade;
    }

    public User getAuthenticatedUserFromPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        return user;
    }


    @GetMapping("magazine/{id}")
    @Transactional
    public String getMagazinePageByParameterId(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Magazine magazine = magazineService.getMagazineById(id);
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = ((SecurityUser) authentication.getPrincipal()).getUser();
            Set<Magazine> magazines = user.getFollowedMagazines();
            model.addAttribute("isFollowMagazine", false);
            for (Magazine m : magazines) {
                if (m.equals(magazine)) {
                    model.addAttribute("isFollowMagazine", true);
                    break;
                }
            }
            if (magazine.getAuthor().equals(user)) {
                model.addAttribute("isFollowMagazine", true);
            }
        } else {
            model.addAttribute("isFollowMagazine", true);
        }

        List<Article> articlesFromMagazine = articleService.getAllArticleByMagazine(magazine);
        Map<Article, String> mapOfUsersImagesInArticleSection = new HashMap<>();
        for (Article article : articlesFromMagazine) {
            String imageString = Base64.getMimeEncoder().encodeToString(article.getImageId().getImageData());
            mapOfUsersImagesInArticleSection.put(article, imageString);
        }
        model.addAttribute("mapOfUsersImagesInArticleSection", mapOfUsersImagesInArticleSection);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        ImageAndModelSettings.getImageStringFromMagazineAndPutInModel(magazine, model);
        model.addAttribute("articles", articlesFromMagazine);
        model.addAttribute("magazine", magazine);
        return "magazine/single_magazine";
    }

    @PostMapping("magazine/{id}/follow")
    public String followMagazineById(@PathVariable("id") Long id) {
        User authenticatedUser = getAuthenticatedUserFromPage();
        Magazine magazineToFollow = magazineService.getMagazineById(id);
        userService.followMagazine(authenticatedUser, magazineToFollow);
        return "redirect:/magazine/" + id;
    }

    @GetMapping("create-magazine")
    public String getMagazineCreationPage(Model model) {
        model.addAttribute("magazine", new Magazine());
        return "magazine/create_magazine";
    }

    @PostMapping("create-magazine")
    public String createMagazine(@ModelAttribute("magazine") Magazine magazine, @RequestParam("imageData") MultipartFile imageData) {
        User user = getAuthenticatedUserFromPage();
        magazine.setAuthor(user);
        saveImageAndSetToMagazine(imageData, magazine);
        magazineService.save(magazine);
        return "redirect:/magazine";
    }

    @PostMapping("delete-magazine/{id}")
    public String deleteMagazineById(@PathVariable("id") Long id) {
        magazineFacade.deleteMagazine(id);
        return "redirect:/";
    }

    @GetMapping("magazine")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getMagazinePageByAuthenticatedUser(Model model) {
        User user = getAuthenticatedUserFromPage();
        Magazine authenticatedUsersMagazine = magazineService.getMagazineByAuthor(user);
        if (authenticatedUsersMagazine == null) {
            return "redirect:/";
        }

        ImageAndModelSettings.getImageStringFromMagazineAndPutInModel(authenticatedUsersMagazine, model);
        model.addAttribute("magazine", authenticatedUsersMagazine);
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);

        List<Article> articlesFromMagazine = articleService.getAllArticleByMagazine(authenticatedUsersMagazine);
        model.addAttribute("articlesFromMagazine", articlesFromMagazine);
        return "magazine/magazine_managing";
    }

    @PostMapping("magazine/update")
    public String updateAuthenticatedUsersMagazine(@ModelAttribute("magazine") Magazine updatedMagazineFromForm,
                                                   @RequestParam("imageData") MultipartFile imageData) {
        if (imageData != null) {
            saveImageAndSetToMagazine(imageData, updatedMagazineFromForm);
        }
        User user = getAuthenticatedUserFromPage();
        Magazine usersMagazineToUpdate = magazineService.getMagazineByAuthor(user);
        magazineService.update(usersMagazineToUpdate.getMagazineId(), updatedMagazineFromForm);
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
}
