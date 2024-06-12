package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

public final class UpdateUserRequest {
    public UpdateUserRequestUser user;

    public UpdateUserRequest() {}

    public UpdateUserRequest(UpdateUserRequestUser user) {
        this.user = user;
    }

    public static final class UpdateUserRequestUser {
        public String email;
        public String username;
        public String password;
        public String bio;
        public String image;

        public UpdateUserRequestUser() {}

        public UpdateUserRequestUser(
                String email, String username, String password, String bio, String image) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.bio = bio;
            this.image = image;
        }
    }
}
