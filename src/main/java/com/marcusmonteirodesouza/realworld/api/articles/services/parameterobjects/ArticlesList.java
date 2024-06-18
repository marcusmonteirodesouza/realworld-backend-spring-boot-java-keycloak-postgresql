package com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects;

import java.util.Collection;
import java.util.Optional;

public class ArticlesList {
    private Optional<String> tag = Optional.empty();
    private Optional<Collection<String>> authorIds = Optional.empty();
    private Optional<String> favoritedByUserId = Optional.empty();
    private Optional<Integer> limit = Optional.empty();
    private Optional<Integer> offset = Optional.empty();

    public ArticlesList() {}

    public ArticlesList(
            Optional<String> tag,
            Optional<Collection<String>> authorIds,
            Optional<String> favoritedByUserId,
            Optional<Integer> limit,
            Optional<Integer> offset) {
        this.tag = tag;
        this.authorIds = authorIds;
        this.favoritedByUserId = favoritedByUserId;
        this.limit = limit;
        this.offset = offset;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public Optional<Collection<String>> getAuthorIds() {
        return authorIds;
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
