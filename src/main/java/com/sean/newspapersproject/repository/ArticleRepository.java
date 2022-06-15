package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    @Modifying
    @Transactional
    @Query("update Article a set a.title = :#{#article.getTitle()}, " +
                    "a.description = :#{#article.getDescription()}, " +
                    "a.text = :#{#article.getText()} " +
                    "where a.articleId = :id")
    void updateArticleById(@Param("id") Long id, @Param("article") Article updatedArticle);

    List<Article> findAllByUserId(User user);

    List<Article> findAllByMagazine(Magazine magazine);

    List<Article> findAllByTitle(String articleTitle);
}
