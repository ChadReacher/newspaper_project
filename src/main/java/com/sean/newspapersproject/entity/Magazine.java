package com.sean.newspapersproject.entity;

import javax.persistence.*;

@Entity
public class Magazine {

    @Id
    @SequenceGenerator(
            name = "magazine_sequence",
            sequenceName = "magazine_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "magazine_sequence")
    private Long magazineId;
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image imageId;

    public Magazine(String name, Image imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public Magazine() {

    }

    public Image getImageId() {
        return imageId;
    }

    public void setImageId(Image imageId) {
        this.imageId = imageId;
    }

    public Long getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(Long magazineId) {
        this.magazineId = magazineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
