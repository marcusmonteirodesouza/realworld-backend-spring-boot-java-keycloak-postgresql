package com.marcusmonteirodesouza.realworld.api.articles.controllers.dto;

import com.marcusmonteirodesouza.realworld.api.articles.controllers.dto.ArticleResponse.ArticleResponseArticle;
import java.util.List;

public class MultipleArticlesResponse {
    private final List<ArticleResponseArticle> articles;
    private final Integer articlesCount;

    public MultipleArticlesResponse(List<ArticleResponseArticle> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

    public List<ArticleResponseArticle> getArticles() {
        return articles;
    }

    public Integer getArticlesCount() {
        return articlesCount;
    }
}
