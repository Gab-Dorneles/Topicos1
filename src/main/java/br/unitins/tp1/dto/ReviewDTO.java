package br.unitins.tp1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReviewDTO {
    
    public Long id;
    
    @NotNull(message = "ID do usuário é obrigatório")
    public Long userId;
    
    @NotNull(message = "ID da skin é obrigatório")
    public Long skinId;
    
    @Min(value = 1, message = "A classificação mínima é 1")
    @Max(value = 5, message = "A classificação máxima é 5")
    public Integer rating;
    
    public String comment;
    
    public LocalDateTime reviewDate;
    
    // Campos adicionais para exibição
    public String userName;
    public String skinName;
}
