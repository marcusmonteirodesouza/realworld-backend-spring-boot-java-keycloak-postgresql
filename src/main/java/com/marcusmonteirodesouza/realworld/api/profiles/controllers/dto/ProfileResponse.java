package com.marcusmonteirodesouza.realworld.api.profiles.controllers.dto;

public final class ProfileResponse {
    private final ProfileResponseProfile profile;

    public ProfileResponse(ProfileResponseProfile profile) {
        this.profile = profile;
    }

    public ProfileResponseProfile getProfile() {
        return profile;
    }

    public static final class ProfileResponseProfile {
        private final String username;
        private final String bio;
        private final String image;
        private final Boolean following;

        public ProfileResponseProfile(
                String username, String bio, String image, Boolean following) {
            this.username = username;
            this.bio = bio;
            this.image = image;
            this.following = following;
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
