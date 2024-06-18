package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import java.util.List;

public class ListOfTagsResponse {
    private final List<String> tags;

    public ListOfTagsResponse(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
