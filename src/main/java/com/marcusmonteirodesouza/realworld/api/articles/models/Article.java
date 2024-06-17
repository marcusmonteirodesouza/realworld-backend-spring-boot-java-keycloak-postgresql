package com.marcusmonteirodesouza.realworld.api.articles.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank private String authorId;

    @NotBlank @Unique private String slug;

    @NotBlank private String title;

    @NotBlank private String description;

    @NotBlank private String body;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Collection<Tag> tagList = new ArrayList<Tag>();

    @OneToMany(
            mappedBy = "article",
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    private Collection<Favorite> favorites = new ArrayList<Favorite>();

    @CreationTimestamp private Date createdAt;

    @UpdateTimestamp private Date updatedAt;

    public Article() {}

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Collection<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(Collection<Tag> tagList) {
        this.tagList = tagList;
    }

    public Collection<Favorite> getFavorites() {
        return favorites;
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.setArticle(this);
    }

    public void removeFavorite(Favorite favorite) {
        favorite.setArticle(null);
        this.favorites.remove(favorite);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
