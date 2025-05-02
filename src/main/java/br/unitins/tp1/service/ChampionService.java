package br.unitins.tp1.service;

import br.unitins.tp1.dto.ChampionDTO;
import br.unitins.tp1.model.Champion;
import br.unitins.tp1.model.Skin;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ChampionService {

    // Buscar todos os campeões
    public List<ChampionDTO> findAll() {
        return Champion.<Champion>listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar campeão por ID
    public ChampionDTO findById(Long id) {
        Champion champion = Champion.findById(id);
        if (champion == null) {
            throw new NotFoundException("Campeão com ID " + id + " não encontrado");
        }
        return toDTO(champion);
    }

    // Buscar campeões por função
    public List<ChampionDTO> findByRole(String role) {
        return Champion.<Champion>find("role", role).list().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Criar novo campeão
    @Transactional
    public ChampionDTO create(ChampionDTO championDTO) {
        validateChampionDTO(championDTO);
        
        Champion champion = new Champion();
        updateChampionFromDTO(champion, championDTO);
        
        champion.persist();
        return toDTO(champion);
    }

    // Atualizar campeão existente
    @Transactional
    public ChampionDTO update(Long id, ChampionDTO championDTO) {
        Champion champion = Champion.findById(id);
        if (champion == null) {
            throw new NotFoundException("Campeão com ID " + id + " não encontrado");
        }
        
        validateChampionDTO(championDTO);
        updateChampionFromDTO(champion, championDTO);
        
        return toDTO(champion);
    }

    // Excluir campeão
    @Transactional
    public void delete(Long id) {
        Champion champion = Champion.findById(id);
        if (champion == null) {
            throw new NotFoundException("Campeão com ID " + id + " não encontrado");
        }
        
        // Verificar se há skins associadas
        if (!champion.skins.isEmpty()) {
            throw new WebApplicationException("Não é possível excluir o campeão pois existem skins associadas a ele", 
                    Response.Status.CONFLICT);
        }
        
        champion.delete();
    }

    // Métodos auxiliares
    private void validateChampionDTO(ChampionDTO championDTO) {
        if (championDTO.name == null || championDTO.name.trim().isEmpty()) {
            throw new WebApplicationException("Nome do campeão é obrigatório", 
                    Response.Status.BAD_REQUEST);
        }
    }

    private void updateChampionFromDTO(Champion champion, ChampionDTO championDTO) {
        champion.name = championDTO.name;
        champion.description = championDTO.description;
        champion.role = championDTO.role;
        champion.imageUrl = championDTO.imageUrl;
    }

    public ChampionDTO toDTO(Champion champion) {
        ChampionDTO dto = new ChampionDTO();
        dto.id = champion.id;
        dto.name = champion.name;
        dto.description = champion.description;
        dto.role = champion.role;
        dto.imageUrl = champion.imageUrl;
        
        // Mapear IDs das skins associadas
        if (champion.skins != null && !champion.skins.isEmpty()) {
            dto.skinIds = champion.skins.stream()
                    .map(skin -> skin.id)
                    .collect(Collectors.toList());
        }
        
        return dto;
    }
}