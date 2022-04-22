package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
