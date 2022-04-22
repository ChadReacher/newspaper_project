package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
}
