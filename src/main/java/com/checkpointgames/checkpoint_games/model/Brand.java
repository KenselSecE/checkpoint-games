package com.checkpointgames.checkpoint_games.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 40, message = "Brand name must be between 2 and 40 characters")
    @Column(nullable = false, unique = true, length = 40)
    private String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Size(max = 300, message = "Description cannot exceed 300 characters")
    @Column(length = 300)
    private String description;

    @Size(max = 40, message = "Country cannot exceed 40 characters")
    @Column(length = 40)
    private String country;
}