package com.marcusmonteirodesouza.realworld.api.articles.repositories.articles;

import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArticlesRepository
        extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {
    public Article getArticleBySlug(String slug);
}
