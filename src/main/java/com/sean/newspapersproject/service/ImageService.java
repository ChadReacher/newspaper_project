package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.*;
import com.sean.newspapersproject.repository.ImageRepository;
import com.sean.newspapersproject.repository.MagazineRepository;
import com.sean.newspapersproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImageById(Long id) {
        Image image = imageRepository.findById(id).get();
        return image;
    }

    public List<Image> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images;
    }

    @Transactional
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Transactional
    public void update(Long id, Image image) {
        Image imageToUpdate = getImageById(id);
        imageToUpdate.setName(image.getName());
        imageToUpdate.setImageData(image.getImageData());
        imageRepository.save(imageToUpdate);
    }

    @Transactional
    public void delete(Long id) {
        Image imageToDelete = getImageById(id);
        imageRepository.delete(imageToDelete);
    }
}
