package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    @Modifying
    @Transactional
    @Query("update Article a set a.title = :#{#article.getTitle()}, " +
                    "a.description = :#{#article.getDescription()}, " +
                    "a.text = :#{#article.getText()} " +
                    "where a.articleId = :id")
    void updateArticleById(@Param("id") Long id, @Param("article") Article updatedArticle);

}
