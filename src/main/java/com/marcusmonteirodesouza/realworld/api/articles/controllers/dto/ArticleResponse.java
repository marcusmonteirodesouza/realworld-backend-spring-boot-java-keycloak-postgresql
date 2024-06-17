package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import java.util.Collection;
import java.util.Date;

public class ArticleResponse {
    private final ArticleResponseArticle article;

    public ArticleResponse(ArticleResponseArticle article) {
        this.article = article;
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
                String slug,
                String title,
                String description,
                String body,
                Collection<String> tagList,
                Date createdAt,
                Date updatedAt,
                Boolean favorited,
                Integer favoritesCount,
                ArticleResponseAuthor author) {
            this.slug = slug;
            this.title = title;
            this.description = description;
            this.body = body;
            this.tagList = tagList;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.favorited = favorited;
            this.favoritesCount = favoritesCount;
            this.author = author;
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
