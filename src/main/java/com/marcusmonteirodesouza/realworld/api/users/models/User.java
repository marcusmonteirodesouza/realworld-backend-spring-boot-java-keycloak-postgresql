package com.marcusmonteirodesouza.realworld.api.users.models;

import com.google.common.base.Optional;

public class User {
    private String email;
    private String username;
    private Optional<String> bio;
    private Optional<String> image;

    public User() {}

    public User(String email, String username, Optional<String> bio, Optional<String> image) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Optional<String> getBio() {
        return bio;
    }

    public void setBio(Optional<String> bio) {
        this.bio = bio;
    }

    public Optional<String> getImage() {
        return image;
    }

    public void setImage(Optional<String> image) {
        this.image = image;
    }
}
