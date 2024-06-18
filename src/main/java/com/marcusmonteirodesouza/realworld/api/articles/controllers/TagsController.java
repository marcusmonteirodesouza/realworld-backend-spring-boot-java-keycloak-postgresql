package com.marcusmonteirodesouza.realworld.api.articles.controllers;

import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ListOfTagsResponse;
import com.marcusmonteirodesouza.realworld.api.articles.services.ArticlesService;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tags")
public class TagsController {
    private final ArticlesService articlesService;

    public TagsController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping()
    public ListOfTagsResponse getTags() {
        var tags = articlesService.listTags();

        var listOfTags =
                tags.stream().map(tag -> tag.getValue()).sorted().collect(Collectors.toList());

        Collections.sort(listOfTags);

        return new ListOfTagsResponse(listOfTags);
    }
}
