package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    
    public Long id;
    
    @NotNull(message = "ID do usuário é obrigatório")
    public Long userId;
    
    public LocalDateTime orderDate;
    
    public BigDecimal totalAmount;
    
    public String status;
    
    public List<OrderItemDTO> items = new ArrayList<>();
}