package com.marcusmonteirodesouza.realworld.api.articles.services;

import com.github.slugify.Slugify;
import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import com.marcusmonteirodesouza.realworld.api.articles.models.Tag;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.ArticlesRepository;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.FavoritesRepository;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.TagsRepository;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleCreate;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ArticlesService {
    private final UsersService usersService;
    private final ArticlesRepository articlesRepository;
    private final TagsRepository tagsRepository;
    private final FavoritesRepository favoritesRepository;

    private final Slugify slg = Slugify.builder().build();

    public ArticlesService(
            UsersService usersService,
            ArticlesRepository articlesRepository,
            TagsRepository tagsRepository,
            FavoritesRepository favoritesRepository) {
        this.usersService = usersService;
        this.articlesRepository = articlesRepository;
        this.tagsRepository = tagsRepository;
        this.favoritesRepository = favoritesRepository;
    }

    public Article createArticle(ArticleCreate articleCreate) {
        var author = usersService.getUserById(articleCreate.getAuthorId()).orNull();

        if (author == null) {
            throw new NotFoundException("Author '" + articleCreate.getAuthorId() + "' not found");
        }

        var tagList = new ArrayList<Tag>();

        if (articleCreate.getTagList().isPresent()) {
            tagList.addAll(
                    new HashSet<String>(articleCreate.getTagList().get())
                            .stream()
                                    .map(tagValue -> new Tag(tagValue))
                                    .collect(Collectors.toList()));
        }

        articleCreate.getTagList();

        var article =
                new Article(
                        author.getId(),
                        makeSlug(articleCreate.getTitle()),
                        articleCreate.getTitle(),
                        articleCreate.getDescription(),
                        articleCreate.getBody(),
                        tagList);

        articlesRepository.save(article);

        return article;
    }

    private String makeSlug(String title) {
        return slg.slugify(title);
    }
}
