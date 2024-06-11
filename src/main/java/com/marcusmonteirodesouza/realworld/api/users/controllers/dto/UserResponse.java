package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

public class UserResponse {
    private final UserResponseUser user;

    public UserResponse(UserResponseUser user) {
        this.user = user;
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

        public UserResponseUser(
                String email, String username, String token, String bio, String image) {
            this.email = email;
            this.username = username;
            this.token = token;
            this.bio = bio;
            this.image = image;
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
