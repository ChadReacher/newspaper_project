package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId (User user);
    List<Comment> findByArticleId(Article article);

    @Modifying
    @Transactional
    @Query("update Comment cm set cm.message = :#{#comment.getMessage()}, " +
            "cm.createdAt = :#{#comment.getCreatedAt()} " +
            "where cm.commentId = :id")
    void updateCommentById(@Param("id") Long id, @Param("comment") Comment updatedComment);

}
