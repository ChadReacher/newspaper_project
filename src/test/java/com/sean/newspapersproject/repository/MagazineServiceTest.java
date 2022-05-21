package com.sean.newspapersproject.repository;

import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.service.MagazineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class MagazineServiceTest {

    @Autowired
    private MagazineService magazineService;

    @Test
    public void testSaveMagazine() {
        Magazine magazine = new Magazine("Fashion Magazine", null, null);
        magazineService.save(magazine);
    }

    @Test
    public void testGetMagazine() {
        Magazine expectedMagazine = new Magazine("Fashion Magazine", null, null);
        Magazine actualMagazine = magazineService.getMagazineByName("Fashion Magazine");
        assertEquals(expectedMagazine.getName(), actualMagazine.getName());
    }
}