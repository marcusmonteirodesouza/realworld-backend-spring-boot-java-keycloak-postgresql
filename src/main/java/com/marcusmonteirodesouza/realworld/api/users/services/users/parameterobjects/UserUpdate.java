package com.marcusmonteirodesouza.realworld.api.users.services.users.parameterobjects;

import java.util.Optional;

public final class UserUpdate {
    private Optional<String> email = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> bio = Optional.empty();
    private Optional<String> image = Optional.empty();

    public UserUpdate() {
        super();
    }

    public UserUpdate(
            Optional<String> email,
            Optional<String> username,
            Optional<String> password,
            Optional<String> bio,
            Optional<String> image) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public Optional<String> getBio() {
        return bio;
    }

    public Optional<String> getImage() {
        return image;
    }
}
