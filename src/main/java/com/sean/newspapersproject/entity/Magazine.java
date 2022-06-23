package com.sean.newspapersproject.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany(mappedBy = "followedMagazines")
    private Set<User> followedUsers;

    public Magazine(String name, Image imageId, User author) {
        this.name = name;
        this.imageId = imageId;
        this.author = author;
    }

    public Magazine() {

    }

    public Set<User> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(Set<User> followedUsers) {
        this.followedUsers = followedUsers;
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
                ", author=" + author.getUsername() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magazine magazine = (Magazine) o;
        return Objects.equals(name, magazine.name) && Objects.equals(author, magazine.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageId);
    }
}
