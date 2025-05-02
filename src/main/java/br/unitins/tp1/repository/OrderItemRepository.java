package br.unitins.tp1.repository;

import br.unitins.tp1.model.Order;
import br.unitins.tp1.model.OrderItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepository<OrderItem> {
    
    public List<OrderItem> findByOrder(Order order) {
        return list("order", order);
    }
}