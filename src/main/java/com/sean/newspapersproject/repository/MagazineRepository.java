package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Magazine findByName(String name);
}
