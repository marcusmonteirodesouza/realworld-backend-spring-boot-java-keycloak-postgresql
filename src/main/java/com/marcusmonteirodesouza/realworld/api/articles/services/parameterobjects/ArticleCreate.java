package com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects;

import java.util.Collection;
import java.util.Optional;

public class ArticleCreate {
    private final String authorId;
    private final String title;
    private final String description;
    private final String body;
    private final Optional<Collection<String>> tagList;

    public ArticleCreate(String authorId, String title, String description, String body) {
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = Optional.empty();
    }

    public ArticleCreate(
            String authorId,
            String title,
            String description,
            String body,
            Optional<Collection<String>> tagList) {
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }

    public String getAuthorId() {
        return authorId;
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

    public Optional<Collection<String>> getTagList() {
        return tagList;
    }
}
