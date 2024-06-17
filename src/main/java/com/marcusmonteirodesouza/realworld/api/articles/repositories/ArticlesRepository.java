package com.marcusmonteirodesouza.realworld.api.articles.repositories;

import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlesRepository extends JpaRepository<Article, String> {
    public Article getArticleBySlug(String slug);
}
