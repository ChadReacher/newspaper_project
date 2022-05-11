package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.Image;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.security.SecurityUser;
import com.sean.newspapersproject.service.ImageService;
import com.sean.newspapersproject.service.UserService;
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
import java.util.Base64;

@Controller
@RequestMapping("/")
public class AuthController {

    private ImageService imageService;
    private UserService userService;

    @Autowired
    public AuthController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("register")
    public String getRegistrationPage() {
        return "registration_page";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("image") MultipartFile imageData)
            throws IOException {
        saveImageAndSetToUser(imageData, user);
        userService.save(user);
        return "redirect:/login";
    }

    public void saveImageAndSetToUser(MultipartFile imageData, User user) throws IOException {
        if (imageData.isEmpty()) {
            System.out.println("This is empty");
        } else {
            String fileName = StringUtils.cleanPath(imageData.getOriginalFilename());
            Image image = new Image(fileName, imageData.getBytes());
            imageService.save(image);
            user.setImageId(image);
        }
    }

    @GetMapping("user/{id}")
    public String getGeneralUserByIdPage(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("authenticated", "false");
        } else {
            model.addAttribute("authenticated", "true");
            User user = ((SecurityUser) authentication.getPrincipal()).getUser();
            String imageString = Base64.getMimeEncoder().encodeToString(user.getImageId().getImageData());
            model.addAttribute("imageString", imageString);
        }
        User user = userService.getUserById(id);
        String imageString = getImageStringFromImageBytes(user.getImageId().getImageData());
        model.addAttribute("authenticatedUserImageString", imageString);
        model.addAttribute("user", user);
        return "general_user_profile";
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('USER')")
    public String getAuthenticatedUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        String imageString = getImageStringFromImageBytes(user.getImageId().getImageData());
        model.addAttribute("imageString", imageString);
        model.addAttribute("user", user);
        return "user_profile";
    }

    public String getImageStringFromImageBytes(byte[] imageData) {
        return Base64.getMimeEncoder().encodeToString(imageData);
    }
}
