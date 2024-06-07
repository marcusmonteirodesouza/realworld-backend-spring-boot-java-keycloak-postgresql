package com.marcusmonteirodesouza.realworld.api.users.controllers;

import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.LoginRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.RegisterUserRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UserResponse;
import com.marcusmonteirodesouza.realworld.api.users.services.UsersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users")
    public UserResponse registerUser(@RequestBody RegisterUserRequest request)
            throws AlreadyExistsException {
        var user =
                usersService.createUser(
                        request.user.username, request.user.email, request.user.password);

        var token = usersService.getToken(user.getUsername(), request.user.password);

        return new UserResponse(
                user.getEmail(),
                user.getUsername(),
                token,
                user.getBio().orNull(),
                user.getImage().orNull());
    }

    @PostMapping("/users/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        var user = usersService.getUserByEmail(request.user.email);

        var token = usersService.getToken(user.getUsername(), request.user.password);

        return new UserResponse(
                user.getEmail(),
                user.getUsername(),
                token,
                user.getBio().orNull(),
                user.getImage().orNull());
    }
}
