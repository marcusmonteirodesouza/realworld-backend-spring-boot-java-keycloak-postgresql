package com.marcusmonteirodesouza.realworld.api.users.services.users.parameterobjects;

import com.google.common.base.Optional;

public final class UserUpdate {
    private Optional<String> email = Optional.absent();
    private Optional<String> username = Optional.absent();
    private Optional<String> password = Optional.absent();
    private Optional<String> bio = Optional.absent();
    private Optional<String> image = Optional.absent();

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
