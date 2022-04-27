package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.NewspapersProjectApplication;
import com.sean.newspapersproject.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

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
        String expectedCategoryName = "Business";
        String actualCategoryName = categoryRepository.findByName(expectedCategoryName).getName();
        assertEquals(expectedCategoryName, actualCategoryName);
    }
}