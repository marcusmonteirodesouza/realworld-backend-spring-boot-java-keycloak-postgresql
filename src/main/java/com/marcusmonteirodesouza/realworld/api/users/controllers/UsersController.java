package com.marcusmonteirodesouza.realworld.api.users.controllers;

import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.LoginRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.RegisterUserRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UpdateUserRequest;
import com.marcusmonteirodesouza.realworld.api.users.controllers.dto.UserResponse;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import com.marcusmonteirodesouza.realworld.api.users.services.users.parameterobjects.UserUpdate;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    private final IAuthenticationFacade authenticationFacade;
    private final UsersService usersService;

    public UsersController(IAuthenticationFacade authenticationFacade, UsersService usersService) {
        this.authenticationFacade = authenticationFacade;
        this.usersService = usersService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody RegisterUserRequest request)
            throws AlreadyExistsException {
        var user =
                usersService.createUser(
                        request.user.username, request.user.email, request.user.password);

        var token = usersService.getToken(user.getUsername(), request.user.password);

        return new UserResponse(user, token);
    }

    @PostMapping("/users/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        var user = usersService.getUserByEmail(request.user.email).orElse(null);

        if (user == null) {
            throw new NotFoundException("User with email '" + request.user.email + "' not found");
        }

        var token = usersService.getToken(user.getUsername(), request.user.password);

        return new UserResponse(user, token);
    }

    @GetMapping("/user")
    public UserResponse getCurrentUser(
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        var authentication = authenticationFacade.getAuthentication();

        var userId = authentication.getName();

        var user = usersService.getUserById(userId).orElse(null);

        if (user == null) {
            throw new NotFoundException("User with ID '" + userId + "' not found");
        }

        var token = authorizationHeader.split(" ")[1];

        return new UserResponse(user, token);
    }

    @PutMapping("/user")
    @Transactional
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
                                        Optional.ofNullable(request.user.email),
                                        Optional.ofNullable(request.user.username),
                                        Optional.ofNullable(request.user.password),
                                        Optional.ofNullable(request.user.bio),
                                        Optional.ofNullable(request.user.image)))
                        .orElse(null);

        if (user == null) {
            throw new NotFoundException("User with ID '" + userId + "' not found");
        }

        var token = authorizationHeader.split(" ")[1];

        return new UserResponse(user, token);
    }
}
