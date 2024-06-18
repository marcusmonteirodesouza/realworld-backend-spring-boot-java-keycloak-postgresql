package com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects;

import java.util.Optional;

public class ArticleUpdate {
    private Optional<String> title = Optional.empty();
    private Optional<String> description = Optional.empty();
    private Optional<String> body = Optional.empty();

    public ArticleUpdate() {}

    public ArticleUpdate(
            Optional<String> title, Optional<String> description, Optional<String> body) {
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
