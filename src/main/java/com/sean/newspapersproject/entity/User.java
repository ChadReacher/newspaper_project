package com.sean.newspapersproject.entity;

import javax.persistence.*;
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
    private String username;
    private String password;
    private String email;

    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_magazine_map",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn(name = "magazine_id")}
    )
    private Set<Magazine> followedMagazines;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
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

    public Set<Magazine> getFollowedMagazines() {
        return followedMagazines;
    }

    public void setFollowedMagazines(Set<Magazine> followedMagazines) {
        this.followedMagazines = followedMagazines;
    }


}
