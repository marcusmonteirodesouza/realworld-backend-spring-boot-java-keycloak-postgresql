package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

public final class LoginRequest {
    public LoginRequestUser user;

    public LoginRequest() {}

    public LoginRequest(LoginRequestUser user) {
        this.user = user;
    }

    public static class LoginRequestUser {
        public String email;
        public String password;

        public LoginRequestUser() {}

        public LoginRequestUser(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
