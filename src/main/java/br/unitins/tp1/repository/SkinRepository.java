package br.unitins.tp1.repository;

import br.unitins.tp1.model.Skin;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SkinRepository implements PanacheRepository<Skin> {
    
    public List<Skin> findByChampion(String champion) {
        return list("champion", champion);
    }
    
    public List<Skin> findAvailable() {
        return list("available", true);
    }
}