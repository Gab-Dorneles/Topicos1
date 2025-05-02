package br.unitins.tp1.repository;

import br.unitins.tp1.model.Review;
import br.unitins.tp1.model.Skin;
import br.unitins.tp1.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {
    
    public List<Review> findBySkin(Skin skin) {
        return list("skin", skin);
    }
    
    public List<Review> findByUser(User user) {
        return list("user", user);
    }
    
    public Double getAverageRatingForSkin(Long skinId) {
        return find("skin.id", skinId)
                .stream()
                .mapToInt(review -> ((Review) review).rating)
                .average()
                .orElse(0.0);
    }
}