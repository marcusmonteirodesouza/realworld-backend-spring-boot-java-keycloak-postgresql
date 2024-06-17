package com.marcusmonteirodesouza.realworld.api.profiles.services;

import com.google.common.base.Optional;
import com.marcusmonteirodesouza.realworld.api.profiles.models.Follow;
import com.marcusmonteirodesouza.realworld.api.profiles.models.Profile;
import com.marcusmonteirodesouza.realworld.api.profiles.repositories.FollowsRepository;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import jakarta.ws.rs.NotFoundException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfilesService {
    private final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final UsersService usersService;
    private final FollowsRepository followsRepository;

    public ProfilesService(UsersService usersService, FollowsRepository followsRepository) {
        this.usersService = usersService;
        this.followsRepository = followsRepository;
    }

    public Profile getProfile(String userId, Optional<String> followerId) {
        var user = usersService.getUserById(userId).orNull();

        if (user == null) {
            throw new NotFoundException("User '" + userId + "' not found");
        }

        var following = false;
        if (followerId.isPresent()) {
            following = this.isFollowing(followerId.get(), user.getId());
        }

        return new Profile(user.getUsername(), user.getBio(), user.getImage(), following);
    }

    public void followUser(String followerId, String followedId) {
        if (isFollowing(followerId, followedId)) {
            return;
        }

        logger.info("Creating Follow. Follower: " + followerId + ", Followed: " + followedId);

        var follower = usersService.getUserById(followerId).orNull();

        if (follower == null) {
            throw new NotFoundException("Follower '" + followerId + "' not found");
        }

        var followed = usersService.getUserById(followedId).orNull();

        if (followed == null) {
            throw new NotFoundException("Followed '" + followedId + "' not found");
        }

        var follow = new Follow();
        follow.setFollowerId(follower.getId());
        follow.setFollowedId(followed.getId());

        this.followsRepository.save(follow);
    }

    public void unfollowUser(String followerId, String followedId) {
        if (!isFollowing(followerId, followedId)) {
            return;
        }

        logger.info("Deleting Follow. Follower: " + followerId + ", Followed: " + followedId);

        followsRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);
    }

    public Boolean isFollowing(String followerId, String followedId) {
        return this.followsRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }
}
