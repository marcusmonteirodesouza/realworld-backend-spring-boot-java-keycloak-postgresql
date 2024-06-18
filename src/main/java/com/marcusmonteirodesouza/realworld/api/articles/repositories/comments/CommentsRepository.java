package com.marcusmonteirodesouza.realworld.api.articles.repositories.comments;

import com.marcusmonteirodesouza.realworld.api.articles.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, String> {}
