package br.unitins.tp1.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.RoundingMode;

@Entity
@Table(name = "skins")
public class Skin extends PanacheEntity {

    @NotBlank(message = "Nome da skin é obrigatório")
    @Column(name = "name", nullable = false)
    public String name;

    @NotNull(message = "Campeão é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "champion_id", nullable = false)
    public Champion champion;

    @Column(name = "description", length = 2000)
    public String description;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    @Column(name = "price", nullable = false)
    public BigDecimal price;

    @Column(name = "image_url")
    public String imageUrl;

    @Column(name = "release_date")
    public LocalDateTime releaseDate;

    @NotNull(message = "Raridade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", nullable = false)
    public Rarity rarity;

    @Column(name = "is_available")
    public boolean available = true;

    @Column(name = "discount_percentage")
    public Integer discountPercentage;

    @OneToMany(mappedBy = "skin", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Review> reviews = new ArrayList<>();

    public enum Rarity {
        COMMON, EPIC, LEGENDARY, ULTIMATE, MYTHIC
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.skin = this;
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.skin = null;
    }

    public BigDecimal getDiscountedPrice() {
        if (discountPercentage == null || discountPercentage <= 0) {
            return price;
        }
        BigDecimal discountFactor = BigDecimal.ONE.subtract(
            new BigDecimal(discountPercentage).divide(new BigDecimal(100))
        );
        return price.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }

    public Double getAverageRating() {
        if (reviews.isEmpty()) {
            return null;
        }
        return reviews.stream()
            .mapToInt(review -> review.rating)
            .average()
            .orElse(0.0);
    }

    public static List<Skin> findByChampionName(String championName) {
        return find("champion.name", championName).list();
    }

    public static List<Skin> findByRarity(Rarity rarity) {
        return find("rarity", rarity).list();
    }

    public static List<Skin> findAvailable() {
        return find("available", true).list();
    }

    public static List<Skin> findWithDiscount() {
        return find("discountPercentage > 0").list();
    }

    public static List<Skin> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return find("price >= ?1 and price <= ?2", minPrice, maxPrice).list();
    }

    public Skin() {
    }

    public Skin(String name, Champion champion, String description, BigDecimal price,
                String imageUrl, LocalDateTime releaseDate, Rarity rarity,
                boolean available, Integer discountPercentage) {
        this.name = name;
        this.champion = champion;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.rarity = rarity;
        this.available = available;
        this.discountPercentage = discountPercentage;
    }
}
