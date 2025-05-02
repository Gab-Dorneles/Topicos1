package br.unitins.tp1.repository;

import br.unitins.tp1.model.Champion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ChampionRepository implements PanacheRepository<Champion> {
    
    public List<Champion> findByRole(String role) {
        return list("role", role);
    }
    
    public Champion findByName(String name) {
        return find("LOWER(name)", name.toLowerCase()).firstResult();
    }
}