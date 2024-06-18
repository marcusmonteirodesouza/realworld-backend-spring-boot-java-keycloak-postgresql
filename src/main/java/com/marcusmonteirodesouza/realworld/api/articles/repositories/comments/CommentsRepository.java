package com.marcusmonteirodesouza.realworld.api.articles.repositories.comments;

import com.marcusmonteirodesouza.realworld.api.articles.models.Article;
import com.marcusmonteirodesouza.realworld.api.articles.models.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, String> {
    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);
}
