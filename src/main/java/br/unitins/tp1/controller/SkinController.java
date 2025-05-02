package br.unitins.tp1.controller;

import br.unitins.tp1.dto.SkinDTO;
import br.unitins.tp1.model.Skin;
import br.unitins.tp1.service.SkinService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.math.BigDecimal;
import java.util.List;

@Path("/api/skins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkinController {

    @Inject
    SkinService skinService;

    @GET
    public List<SkinDTO> findAll() {
        return skinService.findAll();
    }

    @GET
    @Path("/{id}")
    public SkinDTO findById(@PathParam("id") Long id) {
        return skinService.findById(id);
    }

    @GET
    @Path("/available")
    public List<SkinDTO> findAvailable() {
        return skinService.findAvailable();
    }

    @GET
    @Path("/champion/{championName}")
    public List<SkinDTO> findByChampion(@PathParam("championName") String championName) {
        return skinService.findByChampion(championName);
    }

    @GET
    @Path("/rarity/{rarity}")
    public Response findByRarity(@PathParam("rarity") String rarity) {
        try {
            Skin.Rarity rarityEnum = Skin.Rarity.valueOf(rarity.toUpperCase());
            List<SkinDTO> skins = skinService.findByRarity(rarityEnum);
            return Response.ok(skins).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Raridade inválida. Valores permitidos: COMMON, EPIC, LEGENDARY, ULTIMATE, MYTHIC")
                    .build();
        }
    }

    @GET
    @Path("/discount")
    public List<SkinDTO> findWithDiscount() {
        return skinService.findWithDiscount();
    }

    @GET
    @Path("/price-range")
    public List<SkinDTO> findByPriceRange(
            @QueryParam("min") BigDecimal minPrice,
            @QueryParam("max") BigDecimal maxPrice) {
        
        if (minPrice == null) {
            minPrice = BigDecimal.ZERO;
        }
        
        if (maxPrice == null) {
            maxPrice = new BigDecimal("99999.99");
        }
        
        return skinService.findByPriceRange(minPrice, maxPrice);
    }

    @POST
    public Response create(@Valid SkinDTO skinDTO) {
        SkinDTO created = skinService.create(skinDTO);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public SkinDTO update(@PathParam("id") Long id, @Valid SkinDTO skinDTO) {
        return skinService.update(id, skinDTO);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        skinService.delete(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/discount/{percentage}")
    public SkinDTO applyDiscount(
            @PathParam("id") Long id,
            @PathParam("percentage") Integer percentage) {
        
        return skinService.applyDiscount(id, percentage);
    }

    @PATCH
    @Path("/{id}/availability/{available}")
    public SkinDTO setAvailability(
            @PathParam("id") Long id,
            @PathParam("available") boolean available) {
        
        return skinService.setAvailability(id, available);
    }
    
    // Endpoint estatístico para obter as skins mais populares baseado em avaliações
    @GET
    @Path("/popular")
    public List<SkinDTO> findMostPopular(@QueryParam("limit") @DefaultValue("5") int limit) {
        // Este método precisaria ser implementado no SkinService
        // Por enquanto, retornamos as skins disponíveis como exemplo
        return skinService.findAvailable().stream()
                .limit(limit)
                .toList();
    }
    
    // Endpoint para buscar skins por múltiplos filtros
    @GET
    @Path("/search")
    public List<SkinDTO> search(
            @QueryParam("champion") String championName,
            @QueryParam("rarity") String rarity,
            @QueryParam("minPrice") BigDecimal minPrice,
            @QueryParam("maxPrice") BigDecimal maxPrice,
            @QueryParam("available") Boolean available,
            @QueryParam("hasDiscount") Boolean hasDiscount) {
        
        // Este método precisaria ser implementado no SkinService
        // Por enquanto, retornamos todas as skins como exemplo
        return skinService.findAll();
    }
}