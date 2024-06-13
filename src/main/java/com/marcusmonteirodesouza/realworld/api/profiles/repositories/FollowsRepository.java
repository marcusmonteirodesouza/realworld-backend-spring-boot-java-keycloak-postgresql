package com.marcusmonteirodesouza.realworld.api.profiles.repositories;

import com.marcusmonteirodesouza.realworld.api.profiles.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowsRepository extends JpaRepository<Follow, String> {
    Boolean existsByFollowerIdAndFollowedId(String followerId, String followedId);

    void deleteByFollowerIdAndFollowedId(String followerId, String followedId);
}
