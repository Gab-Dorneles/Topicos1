package br.unitins.tp1.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review extends PanacheEntity {
    
    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    
    @NotNull(message = "Skin é obrigatória")
    @ManyToOne
    @JoinColumn(name = "skin_id", nullable = false)
    public Skin skin;
    
    @Min(value = 1, message = "A classificação mínima é 1")
    @Max(value = 5, message = "A classificação máxima é 5")
    @Column(name = "rating", nullable = false)
    public Integer rating;
    
    @Column(name = "comment", length = 1000)
    public String comment;
    
    @Column(name = "review_date", nullable = false)
    public LocalDateTime reviewDate = LocalDateTime.now();

    public void setSkin(Skin skin2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSkin'");
    }
}