package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.configs.ImageAndModelSettings;
import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Image;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.security.Role;
import com.sean.newspapersproject.security.SecurityUser;
import com.sean.newspapersproject.service.ImageService;
import com.sean.newspapersproject.service.MagazineService;
import com.sean.newspapersproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;

@Controller
@RequestMapping("/")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private ImageService imageService;
    private UserService userService;
    private MagazineService magazineService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, ImageService imageService, UserService userService,
                          UserDetailsService userDetailsService,
                          MagazineService magazineService) {
        this.authenticationManager = authenticationManager;
        this.imageService = imageService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.magazineService = magazineService;
    }

    public User getAuthenticatedUserFromPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        return user;
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
    public String registerUser(@ModelAttribute("user") User user,  @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam("image") MultipartFile imageData, Model model) {
        boolean isUserWithUsernameExists = userService.isUserWithUsernameExists(user.getUsername());
        boolean isUserWithEmailExists = userService.isUserWithEmailExists(user.getEmail());
        boolean isPasswordAndConfirmPasswordEqual = user.getPassword().equals(confirmPassword);
        if (isUserWithUsernameExists) {
            model.addAttribute("userExists", "User with this username already exists");
            return "registration_page";
        } else if (isUserWithEmailExists) {
            model.addAttribute("userExistsWithEmail", "The user with this email already exists.");
            return "registration_page";
        } else if (!isPasswordAndConfirmPasswordEqual) {
            model.addAttribute("passwordsNotSame", "Your passwords are not the same.");
            return "registration_page";
        }
        saveImageAndSetToUser(imageData, user);
        user.setRole(Role.USER);
        userService.save(user);
        return "redirect:/login";
    }

    public void saveImageAndSetToUser(MultipartFile imageData, User user) {
        try {
            String fileName = StringUtils.cleanPath(imageData.getOriginalFilename());
            if (fileName.isEmpty()) {
                user.setImageId(null);
            } else {
                Image image = new Image(fileName, imageData.getBytes());
                imageService.save(image);
                user.setImageId(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("user/{id}")
    public String getGeneralUserByIdPage(@PathVariable("id") Long id, Model model) {
        ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
        User user = userService.getUserById(id);
        try {
            String imageString = Base64.getMimeEncoder().encodeToString(user.getImageId().getImageData());
            model.addAttribute("authenticatedUserImageString", imageString);
        } catch (Exception e) {
            String imageString = "";
            model.addAttribute("authenticatedUserImageString", imageString);
        }
        Magazine magazine = magazineService.getMagazineByAuthor(user);
        model.addAttribute("magazine", magazine);
        model.addAttribute("user", user);
        return "user/general_user_profile";
    }

    @GetMapping("user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    public String getAuthenticatedUserPage(Model model) {
        User user = getAuthenticatedUserFromPage();
        ImageAndModelSettings.getImageStringFromUserAndPutInModel(user, model);
        Set<Magazine> magazines = user.getFollowedMagazines();
        model.addAttribute("followedMagazines", magazines);
        boolean isUserHasMagazine = magazineService.getMagazineByAuthor(user) != null;
        model.addAttribute("isUserHasMagazine", isUserHasMagazine);
        if (isUserHasMagazine) {
            model.addAttribute("magazine", magazineService.getMagazineByAuthor(user));
        }
        model.addAttribute("user", user);
        return "user/user_profile";
    }

    @PostMapping("user/unfollow-magazine/{id}")
    @Transactional
    public String unfollowMagazine(@PathVariable("id") Long id) {
        User user = getAuthenticatedUserFromPage();
        Magazine magazineToUnfollow = magazineService.getMagazineById(id);
        user.unfollowMagazine(magazineToUnfollow);
        userService.save(user);
        return "redirect:/user";
    }

    @GetMapping("update-user")
    public String getUpdateUserPage(Model model) {
        User user = getAuthenticatedUserFromPage();
        ImageAndModelSettings.getImageStringFromUserAndPutInModel(user, model);
        model.addAttribute("user", user);
        return "user/edit_user_profile";
    }

    @PostMapping("update-user/change-image")
    public String changeUserAuthenticatedImage(@RequestParam("imageData") MultipartFile imageData) {
        User user = getAuthenticatedUserFromPage();
        updateUserImage(imageData, user);
        return "redirect:/user";
    }

    public void updateUserImage(MultipartFile imageData, User user) {
        try {
            String fileName = StringUtils.cleanPath(imageData.getOriginalFilename());
            if (!fileName.isEmpty()) {
                Image image = new Image(fileName, imageData.getBytes());
                imageService.save(image);
                user.setImageId(image);
                userService.save(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("update-user")
    public String updateUser(@ModelAttribute("user") User updatedUser, HttpServletRequest request,
                             @RequestParam("confirmPassword") String confirmPassword, Model model) {
        boolean isPasswordAndConfirmPasswordEqual = updatedUser.getPassword().equals(confirmPassword);
        if (confirmPassword != null && !isPasswordAndConfirmPasswordEqual) {
            model.addAttribute("passwordNotSame", "Password are not the same.");
            ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
            return "user/edit_user_profile";
        }
        User userToUpdate = getAuthenticatedUserFromPage();
        userService.update(userToUpdate.getUserId(), updatedUser);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userToUpdate.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities()
            );
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication2 = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication2);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/user";
    }
}
