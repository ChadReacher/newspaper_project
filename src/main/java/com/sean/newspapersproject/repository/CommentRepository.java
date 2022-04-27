package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId (User user);
}
