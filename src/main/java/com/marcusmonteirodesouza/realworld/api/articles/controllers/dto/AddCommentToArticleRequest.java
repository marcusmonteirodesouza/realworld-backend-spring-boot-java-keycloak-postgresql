package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

public class AddCommentToArticleRequest {
    public AddCommentToArticleRequestComment comment;

    public AddCommentToArticleRequest() {}

    public AddCommentToArticleRequest(AddCommentToArticleRequestComment comment) {
        this.comment = comment;
    }

    public static final class AddCommentToArticleRequestComment {
        public String body;

        public AddCommentToArticleRequestComment() {}

        public AddCommentToArticleRequestComment(String body) {
            this.body = body;
        }
    }
}
