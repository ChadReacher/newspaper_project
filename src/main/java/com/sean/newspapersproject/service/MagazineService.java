package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MagazineService {

    private MagazineRepository magazineRepository;

    @Autowired
    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }



    public List<Magazine> getAllMagazines() {
        List<Magazine> magazines = magazineRepository.findAll();
        return magazines;
    }

    public Magazine getMagazineById(Long id) {
        Magazine magazine = magazineRepository.findById(id).orElse(null);
        return magazine;
    }

    public Magazine getMagazineByName(String magazineName) {
        Magazine magazine = magazineRepository.findByName(magazineName);
        return magazine;
    }

    public void save(Magazine magazine) {
        magazineRepository.save(magazine);
    }

    @Transactional
    public void update(Long id, Magazine updatedMagazine) {
        magazineRepository.updateMagazineById(id, updatedMagazine);
    }

    @Transactional
    public void delete(Long id) {
        magazineRepository.deleteById(id);
    }
}
