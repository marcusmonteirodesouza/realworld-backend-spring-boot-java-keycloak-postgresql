package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

import com.google.common.base.Optional;

public class UserResponse {
    private String email;
    private String username;
    private String token;
    private Optional<String> bio;
    private Optional<String> image;

    public UserResponse(
            String email,
            String username,
            String token,
            Optional<String> bio,
            Optional<String> image) {
        this.email = email;
        this.username = username;
        this.token = token;
        this.bio = bio;
        this.image = image;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Optional<String> getBio() {
        return this.bio;
    }

    public void setBio(Optional<String> bio) {
        this.bio = bio;
    }

    public Optional<String> getImage() {
        return this.image;
    }

    public void setImage(Optional<String> image) {
        this.image = image;
    }
}
