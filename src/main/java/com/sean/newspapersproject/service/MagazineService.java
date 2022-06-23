package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import com.sean.newspapersproject.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MagazineService {

    private MagazineRepository magazineRepository;
    private ArticleService articleService;

    @Autowired
    public MagazineService(MagazineRepository magazineRepository, ArticleService articleService) {
        this.magazineRepository = magazineRepository;
        this.articleService = articleService;
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
        Magazine magazineToUpdate = getMagazineById(id);
        magazineToUpdate.setName(updatedMagazine.getName());
        magazineToUpdate.setImageId(updatedMagazine.getImageId());
    }

    @Transactional
    public void delete(Magazine magazine) {
        magazineRepository.delete(magazine);
    }

    public Magazine getMagazineByAuthor(User user) {
        Magazine magazine = magazineRepository.findByAuthor(user).orElse(null);
        return magazine;
    }
}
