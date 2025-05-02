package br.unitins.tp1.service;

import br.unitins.tp1.dto.SkinDTO;
import br.unitins.tp1.model.Champion;
import br.unitins.tp1.model.Skin;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SkinService {

    @Inject
    ChampionService championService;

    // Buscar todas as skins
    public List<SkinDTO> findAll() {
        return Skin.<Skin>listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar skin por ID
    public SkinDTO findById(Long id) {
        Skin skin = Skin.findById(id);
        if (skin == null) {
            throw new NotFoundException("Skin com ID " + id + " não encontrada");
        }
        return toDTO(skin);
    }

    // Buscar skins disponíveis
    public List<SkinDTO> findAvailable() {
        return Skin.findAvailable().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar skins por nome do campeão
    public List<SkinDTO> findByChampion(String championName) {
        return Skin.findByChampionName(championName).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar skins por raridade
    public List<SkinDTO> findByRarity(Skin.Rarity rarity) {
        return Skin.findByRarity(rarity).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar skins com desconto
    public List<SkinDTO> findWithDiscount() {
        return Skin.findWithDiscount().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar skins por faixa de preço
    public List<SkinDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return Skin.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Criar nova skin
    @Transactional
    public SkinDTO create(SkinDTO skinDTO) {
        validateSkinDTO(skinDTO);
        
        Skin skin = new Skin();
        updateSkinFromDTO(skin, skinDTO);
        
        skin.persist();
        return toDTO(skin);
    }

    // Atualizar skin existente
    @Transactional
    public SkinDTO update(Long id, SkinDTO skinDTO) {
        Skin skin = Skin.findById(id);
        if (skin == null) {
            throw new NotFoundException("Skin com ID " + id + " não encontrada");
        }
        
        validateSkinDTO(skinDTO);
        updateSkinFromDTO(skin, skinDTO);
        
        skin.persist();
        return toDTO(skin);
    }

    // Excluir skin
    @Transactional
    public void delete(Long id) {
        Skin skin = Skin.findById(id);
        if (skin == null) {
            throw new NotFoundException("Skin com ID " + id + " não encontrada");
        }
        
        // Verificar se há pedidos associados antes de excluir
        // Isso seria implementado se houvesse uma relação com Order
        
        skin.delete();
    }

    // Aplicar desconto a uma skin
    @Transactional
    public SkinDTO applyDiscount(Long id, Integer discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new WebApplicationException("Percentual de desconto deve estar entre 0 e 100", 
                    Response.Status.BAD_REQUEST);
        }
        
        Skin skin = Skin.findById(id);
        if (skin == null) {
            throw new NotFoundException("Skin com ID " + id + " não encontrada");
        }
        
        skin.discountPercentage = discountPercentage;
        skin.persist();
        
        return toDTO(skin);
    }

    // Alterar disponibilidade de uma skin
    @Transactional
    public SkinDTO setAvailability(Long id, boolean available) {
        Skin skin = Skin.findById(id);
        if (skin == null) {
            throw new NotFoundException("Skin com ID " + id + " não encontrada");
        }
        
        skin.available = available;
        skin.persist();
        
        return toDTO(skin);
    }

    // Métodos auxiliares
    private void validateSkinDTO(SkinDTO skinDTO) {
        if (skinDTO.name == null || skinDTO.name.trim().isEmpty()) {
            throw new WebApplicationException("Nome da skin é obrigatório", 
                    Response.Status.BAD_REQUEST);
        }
        
        if (skinDTO.championId == null) {
            throw new WebApplicationException("ID do campeão é obrigatório", 
                    Response.Status.BAD_REQUEST);
        }
        
        if (skinDTO.price == null || skinDTO.price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WebApplicationException("Preço deve ser maior que zero", 
                    Response.Status.BAD_REQUEST);
        }
        
        if (skinDTO.rarity == null) {
            throw new WebApplicationException("Raridade é obrigatória", 
                    Response.Status.BAD_REQUEST);
        }
    }

    private void updateSkinFromDTO(Skin skin, SkinDTO skinDTO) {
        // Buscar o campeão pelo ID
        Champion champion = Champion.findById(skinDTO.championId);
        if (champion == null) {
            throw new NotFoundException("Campeão com ID " + skinDTO.championId + " não encontrado");
        }
        
        skin.name = skinDTO.name;
        skin.champion = champion;
        skin.description = skinDTO.description;
        skin.price = skinDTO.price;
        skin.imageUrl = skinDTO.imageUrl;
        skin.releaseDate = skinDTO.releaseDate != null ? 
                skinDTO.releaseDate : LocalDateTime.now();
        skin.rarity = Skin.Rarity.valueOf(skinDTO.rarity);
        skin.available = skinDTO.available;
        skin.discountPercentage = skinDTO.discountPercentage;
    }

    public SkinDTO toDTO(Skin skin) {
        SkinDTO dto = new SkinDTO();
        dto.id = skin.id;
        dto.name = skin.name;
        dto.championId = skin.champion.id;
        dto.championName = skin.champion.name;
        dto.description = skin.description;
        dto.price = skin.price;
        dto.discountedPrice = skin.getDiscountedPrice();
        dto.imageUrl = skin.imageUrl;
        dto.releaseDate = skin.releaseDate;
        dto.rarity = skin.rarity.name();
        dto.available = skin.available;
        dto.discountPercentage = skin.discountPercentage;
        
        // Calcular média de avaliações se houver
        Double avgRating = skin.getAverageRating();
        dto.averageRating = avgRating;
        
        return dto;
    }
}