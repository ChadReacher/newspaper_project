package com.sean.newspapersproject.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.sean.newspapersproject.NewspapersProjectApplication;
import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.ArticleRepository;
import com.sean.newspapersproject.repository.CategoryRepository;
import com.sean.newspapersproject.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
        User user = new User("username", "username", "user@name.com");
        Category category = new Category("Sport");
        userRepository.save(user);
        categoryRepository.save(category);

        Long articleId = 1L;
        String title = "Test title";
        String description = "Short test description";
        String text = "Some big test text";
        String categoryName = "Sport";
        String formId = "createArticleForm";
        String categorySelectId = "formGroupSelect";

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

        Article article = articleRepository.findById(articleId).get();

        assertNotNull(article);
    }

    @Test
    @Order(4)
    public void testArticlePageByExistingIdExists() throws Exception {
        Long articleId = 1L;
        Article article = articleRepository.findById(articleId).get();
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

        Article article = articleRepository.findById(articleId).get();

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

        boolean articleDoesNotExist = articleRepository.findById(articleId).isEmpty();

        assertTrue(articleDoesNotExist);
    }
}