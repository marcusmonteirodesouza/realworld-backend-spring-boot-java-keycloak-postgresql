package com.marcusmonteirodesouza.realworld.api.users.controllers.dto;

public class UserResponse {
    private final UserResponseUser user;

    public UserResponse(UserResponseUser user) {
        this.user = user;
    }

    public UserResponseUser getUser() {
        return user;
    }
}
