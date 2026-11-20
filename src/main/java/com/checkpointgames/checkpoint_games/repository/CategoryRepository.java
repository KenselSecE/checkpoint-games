package com.checkpointgames.checkpoint_games.repository;

import com.checkpointgames.checkpoint_games.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}