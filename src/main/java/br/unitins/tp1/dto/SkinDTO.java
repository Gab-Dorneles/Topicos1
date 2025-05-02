package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SkinDTO {
    
    public Long id;
    
    @NotBlank(message = "Nome da skin é obrigatório")
    public String name;
    
    @NotNull(message = "ID do campeão é obrigatório")
    public Long championId;
    
    public String championName;
    
    public String description;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    public BigDecimal price;
    
    public BigDecimal discountedPrice;
    
    public String imageUrl;
    
    public LocalDateTime releaseDate;
    
    @NotNull(message = "Raridade é obrigatória")
    public String rarity;
    
    public boolean available = true;
    
    public Integer discountPercentage;
    
    public Double averageRating;
    
    // Construtor padrão
    public SkinDTO() {
    }
    
    // Construtor com parâmetros principais
    public SkinDTO(String name, Long championId, BigDecimal price, String rarity) {
        this.name = name;
        this.championId = championId;
        this.price = price;
        this.rarity = rarity;
        this.available = true;
    }
    
    // Construtor completo
    public SkinDTO(Long id, String name, Long championId, String championName, 
                  String description, BigDecimal price, BigDecimal discountedPrice,
                  String imageUrl, LocalDateTime releaseDate, String rarity,
                  boolean available, Integer discountPercentage, Double averageRating) {
        this.id = id;
        this.name = name;
        this.championId = championId;
        this.championName = championName;
        this.description = description;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.rarity = rarity;
        this.available = available;
        this.discountPercentage = discountPercentage;
        this.averageRating = averageRating;
    }
}