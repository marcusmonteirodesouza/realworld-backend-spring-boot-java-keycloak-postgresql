package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

public class UpdateArticleRequest {
    public UpdateArticleRequestArticle article;

    public UpdateArticleRequest() {}

    public UpdateArticleRequest(UpdateArticleRequestArticle article) {
        this.article = article;
    }

    public static final class UpdateArticleRequestArticle {
        public String title;
        public String description;
        public String body;

        public UpdateArticleRequestArticle() {}

        public UpdateArticleRequestArticle(String title, String description, String body) {
            this.title = title;
            this.description = description;
            this.body = body;
        }
    }
}
