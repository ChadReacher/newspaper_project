package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Magazine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MagazineRepositoryTest {

    @Autowired
    private MagazineRepository magazineRepository;

    @Test
    public void saveMagazine() {
        Magazine magazine = new Magazine("Fashion Magazine");
//        magazineRepository.save(magazine);
    }
}