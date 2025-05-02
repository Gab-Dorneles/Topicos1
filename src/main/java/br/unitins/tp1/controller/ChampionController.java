package br.unitins.tp1.controller;

import br.unitins.tp1.dto.ChampionDTO;
import br.unitins.tp1.service.ChampionService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/champions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChampionController {
    
    @Inject
    ChampionService championService;
    
    @GET
    public List<ChampionDTO> findAll() {
        return championService.findAll();
    }
    
    @GET
    @Path("/{id}")
    public ChampionDTO findById(@PathParam("id") Long id) {
        return championService.findById(id);
    }
    
    @GET
    @Path("/role/{role}")
    public List<ChampionDTO> findByRole(@PathParam("role") String role) {
        return championService.findByRole(role);
    }
    
    @POST
    public Response create(@Valid ChampionDTO championDTO) {
        ChampionDTO created = championService.create(championDTO);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public ChampionDTO update(@PathParam("id") Long id, @Valid ChampionDTO championDTO) {
        return championService.update(id, championDTO);
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        championService.delete(id);
        return Response.noContent().build();
    }
}