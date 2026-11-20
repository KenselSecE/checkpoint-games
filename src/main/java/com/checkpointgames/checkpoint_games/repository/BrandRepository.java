package com.checkpointgames.checkpoint_games.repository;

import com.checkpointgames.checkpoint_games.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}