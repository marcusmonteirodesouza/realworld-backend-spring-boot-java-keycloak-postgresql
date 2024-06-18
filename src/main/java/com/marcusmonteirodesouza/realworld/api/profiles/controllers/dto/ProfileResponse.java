package com.marcusmonteirodesouza.realworld.api.profiles.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.profiles.models.Profile;

public final class ProfileResponse {
    private final ProfileResponseProfile profile;

    public ProfileResponse(Profile profile) {
        this.profile = new ProfileResponseProfile(profile);
    }

    public ProfileResponseProfile getProfile() {
        return profile;
    }

    public static final class ProfileResponseProfile {
        private final String username;
        private final String bio;
        private final String image;
        private final Boolean following;

        public ProfileResponseProfile(Profile profile) {
            this.username = profile.getUsername();
            this.bio = profile.getBio().orElse(null);
            this.image = profile.getImage().orElse(null);
            this.following = profile.getFollowing();
        }

        public String getUsername() {
            return username;
        }

        public String getBio() {
            return bio;
        }

        public String getImage() {
            return image;
        }

        public Boolean getFollowing() {
            return following;
        }
    }
}
