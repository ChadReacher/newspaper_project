package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
