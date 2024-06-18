package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.articles.models.Comment;
import com.marcusmonteirodesouza.realworld.api.profiles.models.Profile;
import java.util.Date;
import java.util.Optional;

public class CommentResponse {
    private final CommentResponseComment comment;

    public CommentResponse(Optional<String> maybeUserId, Comment comment, Profile authorProfile) {
        this.comment = new CommentResponseComment(maybeUserId, comment, authorProfile);
    }

    public CommentResponseComment getComment() {
        return comment;
    }

    public static final class CommentResponseComment {
        private final String id;
        private final Date createdAt;
        private final Date updatedAt;
        private final String body;
        private final CommentResponseAuthor author;

        public CommentResponseComment(
                Optional<String> maybeUserId, Comment comment, Profile authorProfile) {
            this.id = comment.getId();
            this.body = comment.getBody();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.author =
                    new CommentResponseAuthor(
                            authorProfile.getUsername(),
                            authorProfile.getBio().orElse(null),
                            authorProfile.getImage().orElse(null),
                            authorProfile.getFollowing());
        }

        public String getId() {
            return id;
        }

        public String getBody() {
            return body;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public CommentResponseAuthor getAuthor() {
            return author;
        }
    }

    public static final class CommentResponseAuthor {
        private final String username;
        private final String bio;
        private final String image;
        private final Boolean following;

        public CommentResponseAuthor(String username, String bio, String image, Boolean following) {
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
