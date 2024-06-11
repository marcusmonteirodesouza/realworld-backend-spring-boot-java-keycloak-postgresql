package com.marcusmonteirodesouza.realworld.api.users.controllers;

import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.LoginRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.RegisterUserRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UserResponse;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UserResponseUser;
import com.marcusmonteirodesouza.realworld.api.users.services.UsersService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UsersController {
    private final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

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
        var user = usersService.getUserByEmail(request.user.email);

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
    public UserResponse getCurrentUser(@RequestHeader(name = "Authorization") String token) {
        var authentication = authenticationFacade.getAuthentication();

        var user = usersService.getUserById(authentication.getName());

        return new UserResponse(
                new UserResponseUser(
                        user.getEmail(),
                        user.getUsername(),
                        token,
                        user.getBio().orNull(),
                        user.getImage().orNull()));
    }
}
