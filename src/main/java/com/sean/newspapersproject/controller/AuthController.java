package com.sean.newspapersproject.controller;

import com.sean.newspapersproject.entity.Image;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.security.Role;
import com.sean.newspapersproject.security.SecurityUser;
import com.sean.newspapersproject.security.UserDetailsServiceImpl;
import com.sean.newspapersproject.security.config.MainSecurityConfig;
import com.sean.newspapersproject.service.ImageService;
import com.sean.newspapersproject.service.MagazineService;
import com.sean.newspapersproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

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



    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("register")
    public String getRegistrationPage() {
        return "registration_page";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult,  @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam("image") MultipartFile imageData, Model model) {
        User userWithSameUsername = userService.getUserByUsername(user.getUsername());
        User userWithSameEmail = userService.getUserByEmail(user.getEmail());
        if (userWithSameUsername != null) {
            model.addAttribute("userExists", "User with this username already exists");
            return "registration_page";
        } else if (userWithSameEmail != null) {
            model.addAttribute("userExistsWithEmail", "The user with this email already exists.");
            return "registration_page";
        } else if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordsNotSame", "Your passwords are not the same.");
            return "registration_page";
        }
        saveImageAndSetToUser(imageData, user);
        user.setRole(Role.ADMIN);
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

        model.addAttribute("user", user);
        return "general_user_profile";
    }

    @GetMapping("user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getAuthenticatedUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        ImageAndModelSettings.getImageStringFromUserAndPutInModel(user, model);
        boolean isUserHasMagazine = magazineService.getMagazineByAuthor(user) != null;
        model.addAttribute("isUserHasMagazine", isUserHasMagazine);
        if (isUserHasMagazine) {
            model.addAttribute("magazine", magazineService.getMagazineByAuthor(user));
        }
        model.addAttribute("user", user);
        System.out.println(isUserHasMagazine);
        System.out.println(magazineService.getMagazineByAuthor(user));
        System.out.println(user);
        return "user_profile";
    }

    @GetMapping("update-user")
    public String getUpdateUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        ImageAndModelSettings.getImageStringFromUserAndPutInModel(user, model);
        model.addAttribute("user", user);
        return "edit_user_profile";
    }

    @PostMapping("update-user")
    public String updateUser(@ModelAttribute("user") User updatedUser, HttpServletRequest request,
                             @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (confirmPassword != null && !updatedUser.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordNotSame", "Password are not the same.");
            ImageAndModelSettings.updateModelWithAuthenticatedUserAndImageStringFromAuthenticatedUser(model);
            return "edit_user_profile";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userToUpdate = ((SecurityUser) authentication.getPrincipal()).getUser();
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
