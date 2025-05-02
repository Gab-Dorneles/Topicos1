package br.unitins.tp1.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends PanacheEntity {
    
    @NotNull(message = "Pedido é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;
    
    @NotNull(message = "Skin é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", nullable = false)
    public Skin skin;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(name = "quantity", nullable = false)
    public Integer quantity;
    
    @NotNull(message = "Preço é obrigatório")
    @Column(name = "price", nullable = false)
    public BigDecimal price;
}