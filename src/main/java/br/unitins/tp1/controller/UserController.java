package br.unitins.tp1.controller;

import br.unitins.tp1.dto.UserDTO;
import br.unitins.tp1.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    
    @Inject
    UserService userService;
    
    @GET
    public List<UserDTO> findAll() {
        return userService.findAll();
    }
    
    @GET
    @Path("/{id}")
    public UserDTO findById(@PathParam("id") Long id) {
        return userService.findById(id);
    }
    
    @POST
    public Response create(@Valid UserDTO userDTO) {
        UserDTO created = userService.create(userDTO);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public UserDTO update(@PathParam("id") Long id, @Valid UserDTO userDTO) {
        return userService.update(id, userDTO);
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.noContent().build();
    }
}