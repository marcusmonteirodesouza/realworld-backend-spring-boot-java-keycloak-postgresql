package com.marcusmonteirodesouza.realworld.api.articles.services;

import com.github.slugify.Slugify;
import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import com.marcusmonteirodesouza.realworld.api.articles.models.Favorite;
import com.marcusmonteirodesouza.realworld.api.articles.models.Tag;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.ArticlesRepository;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.TagsRepository;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleCreate;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArticlesService {
    private final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final UsersService usersService;
    private final ArticlesRepository articlesRepository;
    private final Slugify slg = Slugify.builder().build();

    public ArticlesService(
            UsersService usersService,
            ArticlesRepository articlesRepository,
            TagsRepository tagsRepository) {
        this.usersService = usersService;
        this.articlesRepository = articlesRepository;
    }

    public Article createArticle(ArticleCreate articleCreate) throws AlreadyExistsException {
        logger.info(
                "Creating article. Author ID: "
                        + articleCreate.getAuthorId()
                        + ", Title: "
                        + articleCreate.getTitle()
                        + ", Description: "
                        + articleCreate.getDescription()
                        + ", Body: "
                        + articleCreate.getBody()
                        + ", TagList"
                        + articleCreate.getTagList());

        var author = usersService.getUserById(articleCreate.getAuthorId()).orElse(null);

        if (author == null) {
            throw new NotFoundException("Author '" + articleCreate.getAuthorId() + "' not found");
        }

        var slug = makeSlug(articleCreate.getTitle());

        if (getArticleBySlug(slug).isPresent()) {
            throw new AlreadyExistsException("Article with slug '" + slug + "' already exists");
        }

        var tagList = new ArrayList<Tag>();

        if (articleCreate.getTagList().isPresent()) {
            tagList.addAll(
                    new HashSet<String>(articleCreate.getTagList().get())
                            .stream()
                                    .map(tagValue -> new Tag(tagValue))
                                    .collect(Collectors.toList()));
        }

        var article = new Article();
        article.setAuthorId(author.getId());
        article.setSlug(slug);
        article.setTitle(articleCreate.getTitle());
        article.setDescription(articleCreate.getDescription());
        article.setBody(articleCreate.getBody());
        article.setTagList(tagList);

        return articlesRepository.saveAndFlush(article);
    }

    public Optional<Article> getArticleById(String articleId) {
        try {
            return Optional.of(articlesRepository.getReferenceById(articleId));
        } catch (EntityNotFoundException ex) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }
    }

    public Optional<Article> getArticleBySlug(String slug) {
        return Optional.ofNullable(articlesRepository.getArticleBySlug(slug));
    }

    public Article favoriteArticle(String userId, String articleId) {
        logger.info("User '" + userId + "' favoriting Article '" + articleId + "'");

        var article = getArticleById(articleId).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }

        if (isFavorited(userId, article)) {
            return article;
        }

        var user = usersService.getUserById(userId).orElse(null);

        if (user == null) {
            throw new NotFoundException("User '" + userId + "' not found");
        }

        var favorite = new Favorite();
        favorite.setUserId(user.getId());

        article.addFavorite(favorite);

        return articlesRepository.saveAndFlush(article);
    }

    public Article unfavoriteArticle(String userId, String articleId) {
        logger.info("User '" + userId + "' unfavoriting Article '" + articleId + "'");

        var article = getArticleById(articleId).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }

        if (!isFavorited(userId, article)) {
            return article;
        }

        var favorite =
                article.getFavorites().stream()
                        .filter(f -> f.getUserId().equals(userId))
                        .findFirst()
                        .get();

        article.removeFavorite(favorite);

        return articlesRepository.saveAndFlush(article);
    }

    private Boolean isFavorited(String userId, Article article) {
        return article.getFavorites().stream()
                .filter(favorite -> favorite.getUserId().equals(userId))
                .findFirst()
                .isPresent();
    }

    private String makeSlug(String title) {
        return slg.slugify(title);
    }
}
