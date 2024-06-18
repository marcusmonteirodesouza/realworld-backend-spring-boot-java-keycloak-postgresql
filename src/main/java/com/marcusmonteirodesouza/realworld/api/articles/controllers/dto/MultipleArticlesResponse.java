package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse.ArticleResponseArticle;
import java.util.Collection;

public class MultipleArticlesResponse {
    private final Collection<ArticleResponseArticle> articles;
    private final Integer articlesCount;

    public MultipleArticlesResponse(Collection<ArticleResponseArticle> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

    public Collection<ArticleResponseArticle> getArticles() {
        return articles;
    }

    public Integer getArticlesCount() {
        return articlesCount;
    }
}
