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

    public Magazine(String name) {
        this.name = name;
    }

    public Magazine() {

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
