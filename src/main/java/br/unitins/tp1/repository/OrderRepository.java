package br.unitins.tp1.repository;

import br.unitins.tp1.model.Order;
import br.unitins.tp1.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
    
    public List<Order> findByUser(User user) {
        return list("user", user);
    }
    
    public List<Order> findByStatus(String status) {
        return list("status", status);
    }
}