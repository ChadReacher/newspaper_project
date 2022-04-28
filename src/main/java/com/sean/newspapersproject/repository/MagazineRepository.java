package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Magazine findByName(String name);

    @Modifying
    @Transactional
    @Query("update Magazine m set m.name = :#{#magazine.getName()} " +
            "where m.magazineId = :id")
    void updateMagazineById(@Param("id") Long id, @Param("magazine") Magazine updatedMagazine);

}
