package com.marcusmonteirodesouza.realworld.api.articles.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    @NotBlank
    private String value;

    @ManyToMany(mappedBy = "tagList", fetch = FetchType.EAGER)
    private Set<Article> articles = new HashSet<Article>();

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
}
