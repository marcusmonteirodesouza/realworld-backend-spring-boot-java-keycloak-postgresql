package com.marcusmonteirodesouza.realworld.api.profiles.models;

import java.util.Optional;

public class Profile {
    private final String username;
    private final Optional<String> bio;
    private final Optional<String> image;
    private final Boolean following;

    public Profile(
            String username, Optional<String> bio, Optional<String> image, Boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getBio() {
        return bio;
    }

    public Optional<String> getImage() {
        return image;
    }

    public Boolean getFollowing() {
        return following;
    }
}
