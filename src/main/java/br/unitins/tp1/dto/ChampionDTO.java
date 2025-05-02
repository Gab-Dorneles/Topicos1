package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ChampionDTO {
    
    public Long id;
    
    @NotBlank(message = "Nome do campeão é obrigatório")
    public String name;
    
    public String description;
    
    public String role;
    
    public String imageUrl;
    
    public List<Long> skinIds;
    
    // Construtor padrão
    public ChampionDTO() {
    }
    
    // Construtor com parâmetros principais
    public ChampionDTO(String name, String description, String role, String imageUrl) {
        this.name = name;
        this.description = description;
        this.role = role;
        this.imageUrl = imageUrl;
    }
    
    // Construtor completo
    public ChampionDTO(Long id, String name, String description, String role, String imageUrl, List<Long> skinIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.role = role;
        this.imageUrl = imageUrl;
        this.skinIds = skinIds;
    }
}