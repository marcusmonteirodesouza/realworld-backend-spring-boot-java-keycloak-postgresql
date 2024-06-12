package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

public final class RegisterUserRequest {
    public RegisterUserRequestUser user;

    public RegisterUserRequest() {}

    public RegisterUserRequest(RegisterUserRequestUser user) {
        this.user = user;
    }

    public static final class RegisterUserRequestUser {
        public String username;
        public String email;
        public String password;

        public RegisterUserRequestUser() {}

        public RegisterUserRequestUser(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
