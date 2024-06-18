package com.marcusmonteirodesouza.realworld.api.profiles.models;

import com.marcusmonteirodesouza.realworld.api.users.models.User;
import java.util.Optional;

public class Profile {
    private final String userId;
    private final String username;
    private final Optional<String> bio;
    private final Optional<String> image;
    private final Boolean following;

    public Profile(
            String userId,
            String username,
            Optional<String> bio,
            Optional<String> image,
            Boolean following) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public Profile(User user, Boolean following) {
        this(user.getId(), user.getUsername(), user.getBio(), user.getImage(), following);
    }

    public String getUserId() {
        return userId;
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
