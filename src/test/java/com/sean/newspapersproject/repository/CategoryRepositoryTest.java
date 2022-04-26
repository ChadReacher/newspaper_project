package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void canGetAllArticles() {
        categoryRepository.findAll();
        verify(categoryRepository).findAll();
    }

    @Test
    public void testSaveCategory() {
        Category actualCategory = new Category("Business");
        Category foundCategory = categoryRepository.findByName("Business");
        if (foundCategory == null) {
            categoryRepository.save(actualCategory);
        }
    }

    @Test
    public void testGetCategory() {
        String expectedCategoryName = "Sport";
        String actualCategoryName = categoryRepository.findByName(expectedCategoryName).getName();
        assertEquals(expectedCategoryName, actualCategoryName);
    }
}