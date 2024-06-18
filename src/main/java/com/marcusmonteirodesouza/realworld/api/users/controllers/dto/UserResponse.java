package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.users.models.User;

public final class UserResponse {
    private final UserResponseUser user;

    public UserResponse(User user, String token) {
        this.user = new UserResponseUser(user, token);
    }

    public UserResponseUser getUser() {
        return user;
    }

    public static final class UserResponseUser {
        private final String email;
        private final String username;
        private final String token;
        private final String bio;
        private final String image;

        public UserResponseUser(User user, String token) {
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.token = token;
            this.bio = user.getBio().orElse(null);
            this.image = user.getImage().orElse(null);
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getToken() {
            return token;
        }

        public String getBio() {
            return bio;
        }

        public String getImage() {
            return image;
        }
    }
}
