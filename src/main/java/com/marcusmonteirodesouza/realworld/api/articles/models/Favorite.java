package com.marcusmonteirodesouza.realworld.api.articles.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank private String userId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Article article;

    @CreationTimestamp private Timestamp createdAt;

    public Favorite() {}

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
