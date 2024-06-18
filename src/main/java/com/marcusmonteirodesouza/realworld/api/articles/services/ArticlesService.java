package com.marcusmonteirodesouza.realworld.api.articles.services;

import com.github.slugify.Slugify;
import com.google.common.base.CaseFormat;
import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import com.marcusmonteirodesouza.realworld.api.articles.models.Comment;
import com.marcusmonteirodesouza.realworld.api.articles.models.Favorite;
import com.marcusmonteirodesouza.realworld.api.articles.models.Tag;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.articles.ArticlesRepository;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.comments.CommentsRepository;
import com.marcusmonteirodesouza.realworld.api.articles.repositories.tags.TagsRepository;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleCreate;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticleUpdate;
import com.marcusmonteirodesouza.realworld.api.articles.services.parameterobjects.ArticlesList;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.services.users.UsersService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
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
    private final TagsRepository tagsRepository;
    private final CommentsRepository commentsRepository;
    private final EntityManager entityManager;
    private final Slugify slg = Slugify.builder().build();

    public ArticlesService(
            UsersService usersService,
            ArticlesRepository articlesRepository,
            TagsRepository tagsRepository,
            CommentsRepository commentsRepository,
            EntityManager entityManager) {
        this.usersService = usersService;
        this.articlesRepository = articlesRepository;
        this.tagsRepository = tagsRepository;
        this.commentsRepository = commentsRepository;
        this.entityManager = entityManager;
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

        var tagList = new HashSet<Tag>();

        if (articleCreate.getTagList().isPresent()) {
            tagList.addAll(
                    articleCreate.getTagList().get().stream()
                            .map(tagValue -> makeTag(tagValue))
                            .collect(Collectors.toSet()));
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

    public List<Article> listArticles(ArticlesList articlesList) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var articleCriteriaQuery = criteriaBuilder.createQuery(Article.class);
        var articleRoot = articleCriteriaQuery.from(Article.class);
        articleCriteriaQuery.select(articleRoot);

        var predicate = criteriaBuilder.conjunction();

        if (articlesList.getTag().isPresent()) {
            var joinArticleTag = articleRoot.join("tagList");
            predicate =
                    criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(
                                    joinArticleTag.get("value"), articlesList.getTag().get()));
        }

        if (articlesList.getAuthorIds().isPresent()) {
            predicate =
                    criteriaBuilder.and(
                            predicate,
                            articleRoot.get("authorId").in(articlesList.getAuthorIds().get()));
        }

        if (articlesList.getFavoritedByUserId().isPresent()) {
            var joinArticleFavorite = articleRoot.join("favorites");
            predicate =
                    criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(
                                    joinArticleFavorite.get("userId"),
                                    articlesList.getFavoritedByUserId().get()));
        }

        articleCriteriaQuery.where(predicate);
        articleCriteriaQuery.orderBy(criteriaBuilder.desc(articleRoot.get("createdAt")));

        var query = entityManager.createQuery(articleCriteriaQuery);

        articlesList.getLimit().ifPresent(query::setMaxResults);
        articlesList.getOffset().ifPresent(query::setFirstResult);

        var articles = query.getResultList();

        for (var article : articles) {
            article.getTagList().stream()
                    .collect(Collectors.toList())
                    .sort((tag1, tag2) -> tag1.getValue().compareTo(tag2.getValue()));
        }

        return articles;
    }

    public Article updateArticle(String articleId, ArticleUpdate articleUpdate) {
        logger.info(
                "Updating Article: '"
                        + articleId
                        + "'. Title: "
                        + articleUpdate.getTitle()
                        + ". Description: "
                        + articleUpdate.getDescription()
                        + ". Body: "
                        + articleUpdate.getBody());

        var article = getArticleById(articleId).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }

        if (articleUpdate.getTitle().isPresent()) {
            var title = articleUpdate.getTitle().get();
            var slug = makeSlug(title);

            article.setSlug(slug);
            article.setTitle(title);
        }

        if (articleUpdate.getDescription().isPresent()) {
            article.setDescription(articleUpdate.getDescription().get());
        }

        if (articleUpdate.getBody().isPresent()) {
            article.setBody(articleUpdate.getBody().get());
        }

        return articlesRepository.saveAndFlush(article);
    }

    public void deleteArticleById(String articleId) {
        logger.info("Deleting Article '" + articleId);

        if (!articlesRepository.existsById(articleId)) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }

        articlesRepository.deleteById(articleId);
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

    public Comment addCommentToArticle(String articleId, String commentAuthorId, String body) {
        logger.info(
                "Adding Comment to Article. Article: "
                        + articleId
                        + ", Author: "
                        + commentAuthorId
                        + ", Body: "
                        + body);

        var article = getArticleById(articleId).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }

        var user = usersService.getUserById(commentAuthorId).orElse(null);

        if (user == null) {
            throw new NotFoundException("User '" + commentAuthorId + "' not found");
        }

        var comment = new Comment();
        comment.setAuthorId(commentAuthorId);
        comment.setBody(body);

        article.addComment(comment);

        return commentsRepository.saveAndFlush(comment);
    }

    public Optional<Comment> getCommentById(String commentId) {
        return commentsRepository.findById(commentId);
    }

    public List<Comment> listCommentsByArticleId(String articleId) {
        var article = getArticleById(articleId).orElse(null);

        if (article == null) {
            throw new NotFoundException("Article '" + articleId + "' not found");
        }

        return commentsRepository.findByArticleOrderByCreatedAtDesc(article);
    }

    public void deleteCommentById(String commentId) {
        logger.info("Deleting Comment '" + commentId + "'");

        var comment = getCommentById(commentId).orElse(null);

        if (comment == null) {
            throw new NotFoundException("Comment '" + commentId + "' not found");
        }

        var article = comment.getArticle();

        article.removeComment(comment);

        articlesRepository.save(article);
    }

    public List<Tag> listTags() {
        return tagsRepository.findAll();
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

    private Tag makeTag(String tagValue) {
        tagValue = tagValue.toLowerCase().trim();
        tagValue = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, tagValue);
        tagValue = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, tagValue);

        var maybeTag = tagsRepository.findByValue(tagValue);

        if (maybeTag.isPresent()) {
            return maybeTag.get();
        } else {
            return new Tag(tagValue);
        }
    }
}
