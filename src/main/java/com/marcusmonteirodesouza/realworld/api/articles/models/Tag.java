package com.marcusmonteirodesouza.realworld.api.articles.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    @NotBlank
    private String value;

    @ManyToMany() private Collection<Article> articles = new ArrayList<Article>();

    public Tag() {}

    public Tag(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Collection<Article> getArticles() {
        return articles;
    }

    public void addArticle(Article article) {
        articles.add(article);
        article.getTagList().add(this);
    }

    public void removeArticle(Article article) {
        this.articles.removeIf(a -> a.getId().equals(article.getId()));
        article.getTagList().removeIf(t -> t.getId().equals(this.getId()));
    }
}
