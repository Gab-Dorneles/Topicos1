package br.unitins.tp1.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                            Response.Status.NOT_FOUND.getStatusCode(),
                            exception.getMessage(),
                            LocalDateTime.now()))
                    .build();
        }
        
        if (exception instanceof IllegalStateException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(
                            Response.Status.BAD_REQUEST.getStatusCode(),
                            exception.getMessage(),
                            LocalDateTime.now()))
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Erro interno do servidor",
                        LocalDateTime.now()))
                .build();
    }
}