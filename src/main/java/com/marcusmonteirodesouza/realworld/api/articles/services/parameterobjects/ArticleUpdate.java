package com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects;

import java.util.Collection;
import java.util.Optional;

public class ArticleUpdate {
    private final Optional<String> title;
    private final Optional<String> description;
    private final Optional<String> body;

    public ArticleUpdate(
            Optional<String> title, Optional<String> description, Optional<String> body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public ArticleUpdate(
            Optional<String> authorId,
            Optional<String> title,
            Optional<String> description,
            Optional<String> body,
            Optional<Collection<Optional<String>>> tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<String> getBody() {
        return body;
    }
}
