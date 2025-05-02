package br.unitins.tp1.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {
    
    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    
    @Column(name = "order_date", nullable = false)
    public LocalDateTime orderDate = LocalDateTime.now();
    
    @Column(name = "total_amount", nullable = false)
    public BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "status", nullable = false)
    public String status = "PENDING";
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<OrderItem> items = new ArrayList<>();
    
    public void calculateTotal() {
        this.totalAmount = items.stream()
                .map(item -> item.price.multiply(new BigDecimal(item.quantity)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}