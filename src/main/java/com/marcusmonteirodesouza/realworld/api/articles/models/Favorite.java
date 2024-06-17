package com.marcusmonteirodesouza.realworld.api.articles.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "articleId"})})
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank private String userId;

    @NotBlank private String articleId;

    @CreationTimestamp private Timestamp createdAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
