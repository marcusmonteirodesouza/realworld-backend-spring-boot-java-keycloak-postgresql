package com.marcusmonteirodesouza.realworld.api.articles.repositories.tags;

import com.marcusmonteirodesouza.realworld.api.articles.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tag, String> {}
