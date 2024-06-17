package com.marcusmonteirodesouza.realworld.api.articles.controllers;

import com.google.common.base.Optional;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.CreateArticleRequest;
import com.marcusmonteirodesouza.realworld.api.articles.services.ArticlesService;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleCreate;
import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.profiles.services.ProfilesService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ArticleResponse createArticle(@RequestBody CreateArticleRequest request)
            throws AlreadyExistsException {
        var userId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article =
                articlesService.createArticle(
                        new ArticleCreate(
                                userId.get(),
                                request.article.title,
                                request.article.description,
                                request.article.body,
                                Optional.fromNullable(request.article.tagList)));

        var authorProfile = profilesService.getProfile(article.getAuthorId(), Optional.absent());

        return new ArticleResponse(userId, article, authorProfile);
    }

    @PostMapping("/{slug}/favorite")
    @Transactional
    public ArticleResponse favoriteArticle(@PathVariable String slug) {
        var userId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orNull();

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        article = articlesService.favoriteArticle(userId.get(), article.getId());

        var authorProfile = profilesService.getProfile(article.getAuthorId(), userId);

        return new ArticleResponse(userId, article, authorProfile);
    }

    @GetMapping("/{slug}")
    public ArticleResponse getArticle(@PathVariable String slug) {
        var maybeUserId = Optional.fromNullable(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orNull();

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        var authorProfile = profilesService.getProfile(article.getAuthorId(), maybeUserId);

        return new ArticleResponse(maybeUserId, article, authorProfile);
    }

    @DeleteMapping("/{slug}/favorite")
    @Transactional
    public ArticleResponse unfavoriteArticle(@PathVariable String slug) {
        var userId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orNull();

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        article = articlesService.unfavoriteArticle(userId.get(), article.getId());

        var authorProfile = profilesService.getProfile(article.getAuthorId(), userId);

        return new ArticleResponse(userId, article, authorProfile);
    }
}
