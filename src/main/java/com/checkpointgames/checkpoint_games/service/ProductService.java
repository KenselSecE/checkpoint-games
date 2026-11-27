package com.checkpointgames.checkpoint_games.service;

import com.checkpointgames.checkpoint_games.model.Brand;
import com.checkpointgames.checkpoint_games.model.Category;
import com.checkpointgames.checkpoint_games.model.Platform;
import com.checkpointgames.checkpoint_games.model.Product;
import com.checkpointgames.checkpoint_games.repository.BrandRepository;
import com.checkpointgames.checkpoint_games.repository.CategoryRepository;
import com.checkpointgames.checkpoint_games.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          BrandRepository brandRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Brand> findAllBrands() {
        return brandRepository.findAll(Sort.by("name"));
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    // Filters (all optional), sorting and pagination done on the server
    public Page<Product> search(Platform platform, Long brandId, Long categoryId,
                                String sortBy, String direction, int page, int size) {
        String field = List.of("name", "price", "createdAt").contains(sortBy) ? sortBy : "createdAt";
        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), size, sort);
        return productRepository.search(platform, brandId, categoryId, pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAll(Sort.by("id"));
    }

    public long count() {
        return productRepository.count();
    }

    public Product update(Long id, Product form) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        existing.setName(form.getName());
        existing.setDescription(form.getDescription());
        existing.setPlatform(form.getPlatform());
        existing.setAgeRating(form.getAgeRating());
        existing.setPrice(form.getPrice());
        existing.setImageUrl(form.getImageUrl());
        existing.setReleaseDate(form.getReleaseDate());
        existing.setBrand(brandRepository.findById(form.getBrand().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand not found")));
        existing.setCategory(categoryRepository.findById(form.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found")));

        return productRepository.save(existing);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }
}