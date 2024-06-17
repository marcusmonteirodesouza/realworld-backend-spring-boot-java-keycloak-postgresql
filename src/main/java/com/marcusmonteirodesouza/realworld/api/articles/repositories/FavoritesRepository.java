package com.marcusmonteirodesouza.realworld.api.articles.repositories;

import com.marcusmonteirodesouza.realworld.api.articles.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorite, String> {}
