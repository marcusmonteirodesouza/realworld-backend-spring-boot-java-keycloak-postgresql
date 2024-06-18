package com.marcusmonteirodesouza.realworld.api.users.models;

import java.util.Optional;
import org.keycloak.representations.idm.UserRepresentation;

public class User {
    private final String id;
    private final String email;
    private final String username;
    private final Optional<String> bio;
    private final Optional<String> image;

    public User(
            String id,
            String email,
            String username,
            Optional<String> bio,
            Optional<String> image) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public User(UserRepresentation userRepresentation) {
        this(
                userRepresentation.getId(),
                userRepresentation.getEmail(),
                userRepresentation.getUsername(),
                Optional.ofNullable(userRepresentation.firstAttribute("bio")),
                Optional.ofNullable(userRepresentation.firstAttribute("image")));
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
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
}
