package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Category;
import com.sean.newspapersproject.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category;
    }

    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        return category;
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void update(Long id, Category updatedCategory) {
        categoryRepository.updateCategoryById(id, updatedCategory);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
