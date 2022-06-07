package com.sean.newspapersproject.configs;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.security.SecurityUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.Base64;

public class ImageAndModelSettings {

    public static void updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("authenticated", "false");
        } else {
            model.addAttribute("authenticated", "true");
            User user = ((SecurityUser) authentication.getPrincipal()).getUser();
            getImageStringFromUserAndPutInModel(user, model);
        }
    }

    public static void getImageStringFromUserAndPutInModel(User user, Model model) {
        try {
            String imageString = Base64.getMimeEncoder().encodeToString(user.getImageId().getImageData());
            model.addAttribute("imageString", imageString);
        } catch (Exception e) {
            String imageString = "";
            model.addAttribute("imageString", imageString);
        }
    }

    public static void getImageStringFromArticleAndPutInModel(Article article, Model model) {
        try {
            String imageString = Base64.getMimeEncoder().encodeToString(article.getImageId().getImageData());
            model.addAttribute("articleImageString", imageString);
        } catch (Exception e) {
            String imageString = "";
            model.addAttribute("articleImageString", imageString);
        }
    }

    public static void getImageStringFromMagazineAndPutInModel(Magazine authenticatedUsersMagazine, Model model) {
        try {
            String imageString = Base64.getMimeEncoder().encodeToString(authenticatedUsersMagazine.getImageId().getImageData());
            model.addAttribute("magazineImage", imageString);
        } catch (Exception e) {
            String imageString = "";
            model.addAttribute("magazineImage", imageString);
        }
    }
}
