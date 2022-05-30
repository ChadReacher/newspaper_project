package com.sean.newspapersproject.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.CategoryRepository;
import com.sean.newspapersproject.repository.UserRepository;
import com.sean.newspapersproject.security.Role;
import com.sean.newspapersproject.service.ArticleService;
import com.sean.newspapersproject.service.CategoryService;
import com.sean.newspapersproject.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
public class MainControllerTest {

    private final String titleInputId = "formGroupExampleInput";
    private final String descriptionInputId = "formGroupExampleInput2";
    private final String textTextAreaId = "exampleFormControlTextarea1";
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    private WebClient webClient;

    @BeforeEach
    void setup(WebApplicationContext context) {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @Order(1)
    public void testMainPageExists() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Online newspaper")));
    }


    @Test
    @Order(2)
    public void testArticleCreationPageExists() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/create-article"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testPostingArticle() throws Exception {
        User user = new User(
                "username", "Tommy", "Resc", "username", "user@name.com", null, Role.USER);
        Category category = new Category("Sport");
        userService.save(user);
        categoryService.save(category);

        Long articleId = 1L;
        String title = "Test title";
        String description = "Short test description";
        String text = "Some big test text";
        String categoryName = "Sport";
        String formId = "createArticleForm";
        String categorySelectId = "formGroupSelect";

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
            );
            Authentication authentication2 = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication2);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }


        HtmlPage createMsgFormPage = webClient.getPage("http://localhost/create-article/");

        HtmlForm form = createMsgFormPage.getHtmlElementById(formId);

        HtmlTextInput titleInput = createMsgFormPage.getHtmlElementById(titleInputId);
        titleInput.setValueAttribute(title);

        HtmlTextInput descriptionInput = createMsgFormPage.getHtmlElementById(descriptionInputId);
        descriptionInput.setValueAttribute(description);

        HtmlSelect categoryInSelect = (HtmlSelect) createMsgFormPage.getElementById(categorySelectId);
        HtmlOption option = categoryInSelect.getOptionByValue(categoryName);
        categoryInSelect.setSelectedAttribute(option, true);

        HtmlTextArea textInTextArea = createMsgFormPage.getHtmlElementById(textTextAreaId);
        textInTextArea.setText(text);

        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        submit.click();

        Article article = articleService.getArticleById(articleId);

        assertNotNull(article);
    }

    @Test
    @Order(4)
    public void testArticlePageByExistingIdExists() throws Exception {
        Long articleId = 1L;
        Article article = articleService.getArticleById(articleId);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/article/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(article.getTitle())))
                .andExpect(content().string(containsString(article.getDescription())))
                .andExpect(content().string(containsString(article.getText())));
    }

    @Test
    @Order(5)
    public void testUpdatingArticle() throws Exception {
        Long articleId = 1L;
        String title = "Test title updated";
        String description = "Short test description updated";
        String text = "Some big test text updated";
        String formId = "updateArticleForm";

        HtmlPage createMsgFormPage = webClient.getPage("http://localhost/update-article/" + articleId);

        HtmlForm form = createMsgFormPage.getHtmlElementById(formId);

        HtmlTextInput titleInput = createMsgFormPage.getHtmlElementById(titleInputId);
        titleInput.setValueAttribute(title);

        HtmlTextInput descriptionInput = createMsgFormPage.getHtmlElementById(descriptionInputId);
        descriptionInput.setValueAttribute(description);


        HtmlTextArea textInTextArea = createMsgFormPage.getHtmlElementById(textTextAreaId);
        textInTextArea.setText(text);

        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        submit.click();

        Article article = articleService.getArticleById(articleId);

        assertNotNull(article);
    }

    @Test
    @Order(6)
    public void testDeleteArticle() throws IOException {
        Long articleId = 1L;
        String formId = "deleteArticleForm";

        HtmlPage createMsgFormPage = webClient.getPage("http://localhost/article/" + articleId);

        HtmlForm form = createMsgFormPage.getHtmlElementById(formId);

        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
        submit.click();

        Article article = articleService.getArticleById(articleId);

        assertNull(article);
    }
}