package com.sean.newspapersproject.entity;

import com.sean.newspapersproject.security.Role;

import javax.persistence.*;
import java.util.Objects;
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
    private String password;
    @Column(unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image imageId;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_magazine_map",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "magazine_id")}
    )
    private Set<Magazine> followedMagazines;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User() {

    }

    public User(String username, String firstName, String lastName, String password, String email, Image imageId, Role role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.imageId = imageId;
        this.role = role;
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

    public void followMagazine(Magazine magazine) {
        this.followedMagazines.add(magazine);
    }

    public void unfollowMagazine(Magazine magazine) {
        this.followedMagazines.remove(magazine);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(username, user.username)
                && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && Objects.equals(password, user.password) && Objects.equals(email, user.email)
                && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, firstName, lastName, password, email, followedMagazines, role);
    }
}
