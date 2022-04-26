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
    public void testSaveMagazine() {
        Magazine magazine = new Magazine("Fashion Magazine");
        magazineRepository.save(magazine);
    }

    @Test
    public void testGetMagazine() {
        Magazine expectedMagazine = new Magazine("Fashion Magazine");
        Magazine actualMagazine = magazineRepository.findByName("Fashion Magazine");
        assertEquals(expectedMagazine.getName(), actualMagazine.getName());
    }
}