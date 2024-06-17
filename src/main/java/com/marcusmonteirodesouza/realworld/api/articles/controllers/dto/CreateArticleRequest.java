package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import java.util.Collection;

public class CreateArticleRequest {
    public CreateArticleRequestArticle article;

    public CreateArticleRequest() {}

    public CreateArticleRequest(CreateArticleRequestArticle article) {
        this.article = article;
    }

    public static final class CreateArticleRequestArticle {
        public String title;
        public String description;
        public String body;
        public Collection<String> tagList;

        public CreateArticleRequestArticle() {}

        public CreateArticleRequestArticle(
                String title, String description, String body, Collection<String> tagList) {
            this.title = title;
            this.description = description;
            this.body = body;
            this.tagList = tagList;
        }
    }
}
