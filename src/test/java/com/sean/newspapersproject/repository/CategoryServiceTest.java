package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testSaveCategory() {
        Category actualCategory = new Category("Business");
        categoryService.save(actualCategory);

        Category actualCategory2 = new Category("Sport");

        Category foundCategory = categoryService.getCategoryByName("Business");
        categoryService.update(1L, actualCategory2);

        Category foundCategory2 = categoryService.getCategoryByName("Sport");
        assertNotNull(foundCategory2);
        assertEquals("Sport", foundCategory2.getName());

    }

    @Test
    public void testGetCategory() {
        Category actualCategory = new Category("Business");
        categoryService.save(actualCategory);
        String expectedCategoryName = "Business";
        String actualCategoryName = categoryService.getCategoryByName(expectedCategoryName).getName();
        assertEquals(expectedCategoryName, actualCategoryName);
    }

}