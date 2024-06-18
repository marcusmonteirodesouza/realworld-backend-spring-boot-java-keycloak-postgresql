package com.marcusmonteirodesouza.realworld.api.profiles.controllers;

import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.profiles.controllers.dto.ProfileResponse;
import com.marcusmonteirodesouza.realworld.api.profiles.services.ProfilesService;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/profiles")
public class ProfilesController {
    private final IAuthenticationFacade authenticationFacade;
    private final UsersService usersService;
    private final ProfilesService profilesService;

    public ProfilesController(
            IAuthenticationFacade authenticationFacade,
            UsersService usersService,
            ProfilesService profilesService) {
        this.authenticationFacade = authenticationFacade;
        this.usersService = usersService;
        this.profilesService = profilesService;
    }

    @PostMapping("/{username}/follow")
    @Transactional
    public ProfileResponse followUser(@PathVariable String username) {
        var user = usersService.getUserByUsername(username).orElse(null);

        if (user == null) {
            throw new NotFoundException("Username '" + username + "' not found");
        }

        var authenticatedUserId = authenticationFacade.getAuthentication().getName();

        profilesService.followUser(authenticatedUserId, user.getId());

        var profile = profilesService.getProfile(user.getId(), Optional.of(authenticatedUserId));

        return new ProfileResponse(profile);
    }

    @GetMapping("/{username}")
    public ProfileResponse getProfile(@PathVariable String username) {
        var user = usersService.getUserByUsername(username).orElse(null);

        if (user == null) {
            throw new NotFoundException("Username '" + username + "' not found");
        }

        var authenticatedUserId = authenticationFacade.getAuthentication().getName();

        var profile =
                profilesService.getProfile(user.getId(), Optional.ofNullable(authenticatedUserId));

        return new ProfileResponse(profile);
    }

    @DeleteMapping("/{username}/follow")
    @Transactional
    public ProfileResponse unfollowUser(@PathVariable String username) {
        var user = usersService.getUserByUsername(username).orElse(null);

        if (user == null) {
            throw new NotFoundException("Username '" + username + "' not found");
        }

        var authenticatedUserId = authenticationFacade.getAuthentication().getName();

        profilesService.unfollowUser(authenticatedUserId, user.getId());

        var profile = profilesService.getProfile(user.getId(), Optional.of(authenticatedUserId));

        return new ProfileResponse(profile);
    }
}
