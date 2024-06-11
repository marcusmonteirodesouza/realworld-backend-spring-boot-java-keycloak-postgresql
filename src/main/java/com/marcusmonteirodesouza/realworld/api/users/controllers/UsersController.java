package com.marcusmonteirodesouza.realworld.api.users.controllers;

import com.google.common.base.Optional;
import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.LoginRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.RegisterUserRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UpdateUserRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UserResponse;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UserResponse.UserResponseUser;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import com.marcusmonteirodesouza.realworld.api.users.services.users.parameterobjects.UserUpdate;
import jakarta.ws.rs.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UsersController {
    private final IAuthenticationFacade authenticationFacade;
    private final UsersService usersService;

    public UsersController(IAuthenticationFacade authenticationFacade, UsersService usersService) {
        this.authenticationFacade = authenticationFacade;
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
                new UserResponseUser(
                        user.getEmail(),
                        user.getUsername(),
                        token,
                        user.getBio().orNull(),
                        user.getImage().orNull()));
    }

    @PostMapping("/users/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        var user = usersService.getUserByEmail(request.user.email).orNull();

        if (user == null) {
            throw new NotFoundException("User with email '" + request.user.email + "' not found");
        }

        var token = usersService.getToken(user.getUsername(), request.user.password);

        return new UserResponse(
                new UserResponseUser(
                        user.getEmail(),
                        user.getUsername(),
                        token,
                        user.getBio().orNull(),
                        user.getImage().orNull()));
    }

    @GetMapping("/user")
    public UserResponse getCurrentUser(
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        var authentication = authenticationFacade.getAuthentication();

        var userId = authentication.getName();

        var user = usersService.getUserById(userId).orNull();

        if (user == null) {
            throw new NotFoundException("User with ID '" + userId + "' not found");
        }

        var token = authorizationHeader.split(" ")[1];

        return new UserResponse(
                new UserResponseUser(
                        user.getEmail(),
                        user.getUsername(),
                        token,
                        user.getBio().orNull(),
                        user.getImage().orNull()));
    }

    @PutMapping("/user")
    public UserResponse updateUser(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody UpdateUserRequest request)
            throws AlreadyExistsException {
        var authentication = authenticationFacade.getAuthentication();

        var userId = authentication.getName();

        var user =
                usersService
                        .updateUser(
                                userId,
                                new UserUpdate(
                                        Optional.fromNullable(request.user.email),
                                        Optional.fromNullable(request.user.username),
                                        Optional.fromNullable(request.user.password),
                                        Optional.fromNullable(request.user.bio),
                                        Optional.fromNullable(request.user.image)))
                        .orNull();

        if (user == null) {
            throw new NotFoundException("User with ID '" + userId + "' not found");
        }

        var token = authorizationHeader.split(" ")[1];

        return new UserResponse(
                new UserResponseUser(
                        user.getEmail(),
                        user.getUsername(),
                        token,
                        user.getBio().orNull(),
                        user.getImage().orNull()));
    }
}
