package com.checkpointgames.checkpoint_games.repository;

import com.checkpointgames.checkpoint_games.model.Platform;
import com.checkpointgames.checkpoint_games.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT p FROM Product p
            WHERE (:platform IS NULL OR p.platform = :platform)
              AND (:brandId IS NULL OR p.brand.id = :brandId)
              AND (:categoryId IS NULL OR p.category.id = :categoryId)
            """)
    Page<Product> search(@Param("platform") Platform platform,
                         @Param("brandId") Long brandId,
                         @Param("categoryId") Long categoryId,
                         Pageable pageable);
}