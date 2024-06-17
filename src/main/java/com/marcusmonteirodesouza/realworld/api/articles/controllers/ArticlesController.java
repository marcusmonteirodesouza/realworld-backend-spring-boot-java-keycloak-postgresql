package com.marcusmonteirodesouza.realworld.api.articles.controllers;

import com.google.common.base.Optional;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse.ArticleResponseArticle;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse.ArticleResponseAuthor;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.CreateArticleRequest;
import com.marcusmonteirodesouza.realworld.api.articles.services.ArticlesService;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleCreate;
import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.profiles.services.ProfilesService;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/articles")
public class ArticlesController {
    private final IAuthenticationFacade authenticationFacade;
    private final ArticlesService articlesService;
    private final ProfilesService profilesService;

    public ArticlesController(
            IAuthenticationFacade authenticationFacade,
            ArticlesService articlesService,
            ProfilesService profilesService) {
        this.authenticationFacade = authenticationFacade;
        this.articlesService = articlesService;
        this.profilesService = profilesService;
    }

    @PostMapping()
    @Transactional
    public ArticleResponse createArticle(@RequestBody CreateArticleRequest request) {
        var authorId = authenticationFacade.getAuthentication().getName();

        var article =
                articlesService.createArticle(
                        new ArticleCreate(
                                authorId,
                                request.article.title,
                                request.article.description,
                                request.article.body,
                                Optional.fromNullable(request.article.tagList)));

        var tagList =
                article.getTagList().stream()
                        .map(tag -> tag.getValue())
                        .collect(Collectors.toList());

        var authorProfile = profilesService.getProfile(authorId, Optional.absent());

        return new ArticleResponse(
                new ArticleResponseArticle(
                        article.getSlug(),
                        article.getTitle(),
                        article.getDescription(),
                        article.getBody(),
                        tagList,
                        article.getCreatedAt(),
                        article.getUpdatedAt(),
                        false,
                        0,
                        new ArticleResponseAuthor(
                                authorProfile.getUsername(),
                                authorProfile.getBio().orNull(),
                                authorProfile.getImage().orNull(),
                                authorProfile.getFollowing())));
    }
}
