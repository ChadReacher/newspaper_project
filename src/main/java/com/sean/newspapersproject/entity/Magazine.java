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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    public Magazine(String name, Image imageId, User author) {
        this.name = name;
        this.imageId = imageId;
        this.author = author;
    }

    public Magazine() {

    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    @Override
    public String toString() {
        return "Magazine{" +
                "magazineId=" + magazineId +
                ", name='" + name + '\'' +
                ", author=" + author +
                '}';
    }
}
