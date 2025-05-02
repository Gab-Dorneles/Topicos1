package br.unitins.tp1.controller;

import br.unitins.tp1.dto.OrderDTO;
import br.unitins.tp1.service.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {
    
    @Inject
    OrderService orderService;
    
    @GET
    public List<OrderDTO> findAll() {
        return orderService.findAll();
    }
    
    @GET
    @Path("/user/{userId}")
    public List<OrderDTO> findByUser(@PathParam("userId") Long userId) {
        return orderService.findByUser(userId);
    }
    
    @GET
    @Path("/{id}")
    public OrderDTO findById(@PathParam("id") Long id) {
        return orderService.findById(id);
    }
    
    @POST
    public Response create(@Valid OrderDTO orderDTO) {
        OrderDTO created = orderService.create(orderDTO);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public OrderDTO update(@PathParam("id") Long id, @Valid OrderDTO orderDTO) {
        return orderService.update(id, orderDTO);
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        orderService.delete(id);
        return Response.noContent().build();
    }
}