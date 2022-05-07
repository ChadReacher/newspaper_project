package com.sean.newspapersproject.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    private Long userId;
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    @Size(min = 5, max = 30, message = "Password should be between 5 and 30 characters")
    private String password;
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image imageId;

    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_magazine_map",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn(name = "magazine_id")}
    )
    private Set<Magazine> followedMagazines;

    public User(String username, String firstName, String lastName, String password, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getImageId() {
        return imageId;
    }

    public void setImageId(Image imageId) {
        this.imageId = imageId;
    }

    public Set<Magazine> getFollowedMagazines() {
        return followedMagazines;
    }

    public void setFollowedMagazines(Set<Magazine> followedMagazines) {
        this.followedMagazines = followedMagazines;
    }
}
