package com.marcusmonteirodesouza.realworld.api.articles.controllers;

import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.AddCommentToArticleRequest;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse.ArticleResponseArticle;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.CommentResponse;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.CommentResponse.CommentResponseComment;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.CreateArticleRequest;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.MultipleArticlesResponse;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.MultipleCommentsResponse;
import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.UpdateArticleRequest;
import com.marcusmonteirodesouza.realworld.api.articles.services.ArticlesService;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleCreate;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleUpdate;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticlesList;
import com.marcusmonteirodesouza.realworld.api.authentication.IAuthenticationFacade;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.profiles.models.Profile;
import com.marcusmonteirodesouza.realworld.api.profiles.services.ProfilesService;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles")
public class ArticlesController {
    private final IAuthenticationFacade authenticationFacade;
    private final ArticlesService articlesService;
    private final UsersService usersService;
    private final ProfilesService profilesService;

    public ArticlesController(
            IAuthenticationFacade authenticationFacade,
            ArticlesService articlesService,
            UsersService usersService,
            ProfilesService profilesService) {
        this.authenticationFacade = authenticationFacade;
        this.articlesService = articlesService;
        this.usersService = usersService;
        this.profilesService = profilesService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ArticleResponse createArticle(@RequestBody CreateArticleRequest request)
            throws AlreadyExistsException {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article =
                articlesService.createArticle(
                        new ArticleCreate(
                                maybeUserId.get(),
                                request.article.title,
                                request.article.description,
                                request.article.body,
                                Optional.ofNullable(request.article.tagList)));

        var authorProfile = profilesService.getProfile(article.getAuthorId(), maybeUserId);

        return new ArticleResponse(maybeUserId, article, authorProfile);
    }

    @PostMapping("/{slug}/favorite")
    @Transactional
    public ArticleResponse favoriteArticle(@PathVariable String slug) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        article = articlesService.favoriteArticle(maybeUserId.get(), article.getId());

        var authorProfile = profilesService.getProfile(article.getAuthorId(), maybeUserId);

        return new ArticleResponse(maybeUserId, article, authorProfile);
    }

    @PostMapping("/{slug}/comments")
    @Transactional
    public CommentResponse addCommentToArticle(
            @PathVariable String slug, @RequestBody AddCommentToArticleRequest request) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        var comment =
                articlesService.addCommentToArticle(
                        article.getId(), maybeUserId.get(), request.comment.body);

        var authorProfile = profilesService.getProfile(comment.getAuthorId(), maybeUserId);

        return new CommentResponse(maybeUserId, comment, authorProfile);
    }

    @GetMapping()
    public MultipleArticlesResponse listArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {
        var maybeUserId = Optional.ofNullable(authenticationFacade.getAuthentication().getName());

        Optional<Collection<String>> authorIds =
                author == null ? Optional.empty() : Optional.of(Arrays.asList(author));

        Optional<String> favoritedByUserId = Optional.empty();
        if (favorited != null) {
            var favoritedByUser = usersService.getUserByUsername(favorited).orElse(null);

            if (favoritedByUser == null) {
                throw new NotFoundException("User with username '" + favorited + "' not found");
            }

            favoritedByUserId = Optional.of(favoritedByUser.getId());
        }

        var articles =
                articlesService.listArticles(
                        new ArticlesList(
                                Optional.ofNullable(tag),
                                authorIds,
                                favoritedByUserId,
                                Optional.of(limit),
                                Optional.of(offset)));

        var articleResponses =
                articles.stream()
                        .map(
                                article -> {
                                    var authorProfile =
                                            profilesService.getProfile(
                                                    article.getAuthorId(), maybeUserId);

                                    return new ArticleResponseArticle(
                                            maybeUserId, article, authorProfile);
                                })
                        .collect(Collectors.toList());

        return new MultipleArticlesResponse(articleResponses);
    }

    @GetMapping("/feed")
    public MultipleArticlesResponse feedArticles(
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var followedUserProfilesMap =
                profilesService.listProfilesFollowedByUserId(maybeUserId.get()).stream()
                        .collect(Collectors.toMap(Profile::getUserId, profile -> profile));

        var articles =
                articlesService.listArticles(
                        new ArticlesList(
                                Optional.empty(),
                                Optional.of(
                                        followedUserProfilesMap.keySet().stream()
                                                .collect(Collectors.toList())),
                                Optional.empty(),
                                Optional.of(limit),
                                Optional.of(offset)));

        var articleResponseArticles =
                articles.stream()
                        .map(
                                article -> {
                                    var authorProfile =
                                            followedUserProfilesMap.get(article.getAuthorId());

                                    return new ArticleResponseArticle(
                                            maybeUserId, article, authorProfile);
                                })
                        .collect(Collectors.toList());

        return new MultipleArticlesResponse(articleResponseArticles);
    }

    @GetMapping("/{slug}")
    public ArticleResponse getArticle(@PathVariable String slug) {
        var maybeUserId = Optional.ofNullable(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        var authorProfile = profilesService.getProfile(article.getAuthorId(), maybeUserId);

        return new ArticleResponse(maybeUserId, article, authorProfile);
    }

    @GetMapping("/{slug}/comments")
    @Transactional
    public MultipleCommentsResponse getArticleComments(@PathVariable String slug) {
        var maybeUserId = Optional.ofNullable(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        var comments = articlesService.listCommentsByArticleId(article.getId());

        var commentResponseComments =
                comments.stream()
                        .map(
                                comment -> {
                                    var authorProfile =
                                            profilesService.getProfile(
                                                    comment.getAuthorId(), maybeUserId);

                                    return new CommentResponseComment(
                                            maybeUserId, comment, authorProfile);
                                })
                        .collect(Collectors.toList());

        return new MultipleCommentsResponse(commentResponseComments);
    }

    @PutMapping("/{slug}")
    @Transactional
    public ArticleResponse updateArticle(
            @PathVariable String slug, @RequestBody UpdateArticleRequest request) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        if (!article.getAuthorId().equals(maybeUserId.get())) {
            throw new ForbiddenException(
                    "User '"
                            + maybeUserId.get()
                            + "' can't update Article with slug '"
                            + slug
                            + '"');
        }

        article =
                articlesService.updateArticle(
                        article.getId(),
                        new ArticleUpdate(
                                Optional.ofNullable(request.article.title),
                                Optional.ofNullable(request.article.description),
                                Optional.ofNullable(request.article.body)));

        var authorProfile = profilesService.getProfile(article.getAuthorId(), maybeUserId);

        return new ArticleResponse(maybeUserId, article, authorProfile);
    }

    @DeleteMapping("/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteArticle(@PathVariable String slug) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        if (!article.getAuthorId().equals(maybeUserId.get())) {
            throw new ForbiddenException(
                    "User '"
                            + maybeUserId.get()
                            + "' cannot delete Article with slug '"
                            + slug
                            + "'");
        }

        articlesService.deleteArticleById(article.getId());
    }

    @DeleteMapping("/{slug}/favorite")
    @Transactional
    public ArticleResponse unfavoriteArticle(@PathVariable String slug) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var article = articlesService.getArticleBySlug(slug).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article with slug '" + slug + "' not found");
        }

        article = articlesService.unfavoriteArticle(maybeUserId.get(), article.getId());

        var authorProfile = profilesService.getProfile(article.getAuthorId(), maybeUserId);

        return new ArticleResponse(maybeUserId, article, authorProfile);
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteArticle(@PathVariable String slug, @PathVariable String commentId) {
        var maybeUserId = Optional.of(authenticationFacade.getAuthentication().getName());

        var comment = articlesService.getCommentById(commentId).orElse(null);

        if (comment == null) {
            throw new NotFoundException("Comment '" + commentId + "' not found");
        }

        if (!comment.getAuthorId().equals(maybeUserId.get())) {
            throw new ForbiddenException(
                    "User '" + maybeUserId.get() + "' cannot delete Comment '" + commentId + "'");
        }

        articlesService.deleteCommentById(commentId);
    }
}
