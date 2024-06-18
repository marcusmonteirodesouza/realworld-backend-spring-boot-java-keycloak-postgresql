package com.marcusmonteirodesouza.realworld.api.articles.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank private String authorId;

    @NotBlank private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    private Article article;

    @CreationTimestamp private Date createdAt;

    @CreationTimestamp private Date updatedAt;

    public Comment() {}

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String userId) {
        this.body = userId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
