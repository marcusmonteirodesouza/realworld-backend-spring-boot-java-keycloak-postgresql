package com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects;

import java.util.Optional;

public class ArticleList {
    private Optional<String> tag = Optional.empty();
    private Optional<String> authorId = Optional.empty();
    private Optional<String> favoritedByUserId = Optional.empty();
    private Optional<Integer> limit = Optional.empty();
    private Optional<Integer> offset = Optional.empty();

    public ArticleList() {}

    public ArticleList(
            Optional<String> tag,
            Optional<String> authorId,
            Optional<String> favoritedByUserId,
            Optional<Integer> limit,
            Optional<Integer> offset) {
        this.tag = tag;
        this.authorId = authorId;
        this.favoritedByUserId = favoritedByUserId;
        this.limit = limit;
        this.offset = offset;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public Optional<String> getAuthorId() {
        return authorId;
    }

    public Optional<String> getFavoritedByUserId() {
        return favoritedByUserId;
    }

    public Optional<Integer> getLimit() {
        return limit;
    }

    public Optional<Integer> getOffset() {
        return offset;
    }
}
