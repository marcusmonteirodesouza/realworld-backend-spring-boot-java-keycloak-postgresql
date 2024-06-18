package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.CommentResponse.CommentResponseComment;
import java.util.List;

public class MultipleCommentsResponse {
    private final List<CommentResponseComment> comments;

    public MultipleCommentsResponse(List<CommentResponseComment> comments) {
        this.comments = comments;
    }

    public List<CommentResponseComment> getComments() {
        return comments;
    }
}
