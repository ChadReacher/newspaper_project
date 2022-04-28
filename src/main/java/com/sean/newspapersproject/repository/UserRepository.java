package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.username = :#{#user.getUsername()}, " +
            "u.password = :#{#user.getPassword()}, " +
            "u.email = :#{#user.getEmail()} " +
            "where u.userId = :id")
    void updateUserById(@Param("id") Long id, @Param("user") User updatedUser);

}
