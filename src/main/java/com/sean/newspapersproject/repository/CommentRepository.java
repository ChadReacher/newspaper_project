package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
