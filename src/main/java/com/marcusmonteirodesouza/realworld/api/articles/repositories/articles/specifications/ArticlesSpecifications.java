package com.marcusmonteirodesouza.realworld.api.articles.repositories.articles.specifications;

import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import com.marcusmonteirodesouza.realworld.api.articles.models.Tag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ArticlesSpecifications {
    public static Specification<Article> hasTag(String tagValue) {
        return (root, query, builder) -> {
            Join<Article, Tag> tags = root.join("tagList", JoinType.INNER);
            return builder.equal(tags.get("value"), tagValue);
        };
    }
}
