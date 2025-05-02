package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class OrderItemDTO {
    
    public Long id;
    
    public Long orderId;
    
    @NotNull(message = "ID da skin é obrigatório")
    public Long skinId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    public Integer quantity;
    
    public BigDecimal price;
}