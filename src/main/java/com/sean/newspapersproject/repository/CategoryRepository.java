package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String categoryName);

    @Modifying
    @Transactional
    @Query("update Category c set c.name = :#{#category.getName()} where c.categoryId = :id")
    void updateCategoryById(@Param("id") Long id, @Param("category") Category updatedCategory);
}
