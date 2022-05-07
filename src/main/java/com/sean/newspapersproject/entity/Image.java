package com.sean.newspapersproject.entity;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @SequenceGenerator(
            name = "image_sequence",
            sequenceName = "image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sequence")
    private Long imageId;

    private String name;

    private byte[] imageData;

    public Image(String name, byte[] imageData) {
        this.name = name;
        this.imageData = imageData;
    }

    public Image() {

    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
