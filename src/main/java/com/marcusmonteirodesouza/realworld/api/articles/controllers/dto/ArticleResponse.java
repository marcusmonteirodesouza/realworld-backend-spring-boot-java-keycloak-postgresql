package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import com.marcusmonteirodesouza.realworld.api.profiles.models.Profile;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class ArticleResponse {
    private final ArticleResponseArticle article;

    public ArticleResponse(Optional<String> maybeUserId, Article article, Profile authorProfile) {
        this.article = new ArticleResponseArticle(maybeUserId, article, authorProfile);
    }

    public ArticleResponseArticle getArticle() {
        return article;
    }

    public static final class ArticleResponseArticle {
        private final String slug;
        private final String title;
        private final String description;
        private final String body;
        private final Collection<String> tagList;
        private final Date createdAt;
        private final Date updatedAt;
        private final Boolean favorited;
        private final Integer favoritesCount;
        private final ArticleResponseAuthor author;

        public ArticleResponseArticle(
                Optional<String> maybeUserId, Article article, Profile authorProfile) {
            this.slug = article.getSlug();
            this.title = article.getTitle();
            this.description = article.getDescription();
            this.body = article.getBody();
            this.tagList = article.getTagList().stream().map(tag -> tag.getValue()).toList();
            this.createdAt = article.getCreatedAt();
            this.updatedAt = article.getUpdatedAt();
            this.favorited =
                    maybeUserId.isPresent()
                            ? article.getFavorites().stream()
                                    .filter(
                                            favorite ->
                                                    favorite.getUserId().equals(maybeUserId.get()))
                                    .findFirst()
                                    .isPresent()
                            : false;
            this.favoritesCount = article.getFavorites().size();
            this.author =
                    new ArticleResponseAuthor(
                            authorProfile.getUsername(),
                            authorProfile.getBio().orElse(null),
                            authorProfile.getImage().orElse(null),
                            authorProfile.getFollowing());
        }

        public String getSlug() {
            return slug;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBody() {
            return body;
        }

        public Collection<String> getTagList() {
            return tagList;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public Boolean getFavorited() {
            return favorited;
        }

        public Integer getFavoritesCount() {
            return favoritesCount;
        }

        public ArticleResponseAuthor getAuthor() {
            return author;
        }
    }

    public static final class ArticleResponseAuthor {
        private final String username;
        private final String bio;
        private final String image;
        private final Boolean following;

        public ArticleResponseAuthor(String username, String bio, String image, Boolean following) {
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
