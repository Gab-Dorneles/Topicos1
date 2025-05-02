package br.unitins.tp1.service;

import br.unitins.tp1.dto.ReviewDTO;
import br.unitins.tp1.model.Review;
import br.unitins.tp1.model.Skin;
import br.unitins.tp1.model.User;
import br.unitins.tp1.repository.ReviewRepository;
import br.unitins.tp1.repository.SkinRepository;
import br.unitins.tp1.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReviewService {
    
    @Inject
    ReviewRepository reviewRepository;
    
    @Inject
    UserRepository userRepository;
    
    @Inject
    SkinRepository skinRepository;
    
    public List<ReviewDTO> findAll() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public ReviewDTO findById(Long id) {
        Review review = reviewRepository.findById(id);
        if (review == null) {
            throw new NotFoundException("Avaliação não encontrada com ID: " + id);
        }
        return mapToDTO(review);
    }
    
    public List<ReviewDTO> findBySkinId(Long skinId) {
        Skin skin = skinRepository.findById(skinId);
        if (skin == null) {
            throw new NotFoundException("Skin não encontrada com ID: " + skinId);
        }
        return reviewRepository.findBySkin(skin).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReviewDTO> findByUserId(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + userId);
        }
        return reviewRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public Double getAverageRatingForSkin(Long skinId) {
        return reviewRepository.getAverageRatingForSkin(skinId);
    }
    
    @Transactional
    public ReviewDTO create(ReviewDTO reviewDTO) {
        // Verificar se o usuário já avaliou esta skin
        User user = userRepository.findById(reviewDTO.userId);
        Skin skin = skinRepository.findById(reviewDTO.skinId);
        
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + reviewDTO.userId);
        }
        
        if (skin == null) {
            throw new NotFoundException("Skin não encontrada com ID: " + reviewDTO.skinId);
        }
        
        // Verificar se o usuário já avaliou esta skin
        List<Review> existingReviews = reviewRepository.find("user.id = ?1 and skin.id = ?2", 
                reviewDTO.userId, reviewDTO.skinId).list();
        
        if (!existingReviews.isEmpty()) {
            throw new IllegalStateException("Usuário já avaliou esta skin");
        }
        
        Review review = mapToEntity(reviewDTO);
        review.reviewDate = LocalDateTime.now();
        reviewRepository.persist(review);
        
        return mapToDTO(review);
    }
    
    @Transactional
    public ReviewDTO update(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id);
        if (review == null) {
            throw new NotFoundException("Avaliação não encontrada com ID: " + id);
        }
        
        // Verificar se o usuário é o mesmo que criou a avaliação
        if (!review.user.id.equals(reviewDTO.userId)) {
            throw new IllegalStateException("Apenas o usuário que criou a avaliação pode atualizá-la");
        }
        
        review.rating = reviewDTO.rating;
        review.comment = reviewDTO.comment;
        
        return mapToDTO(review);
    }
    
    @Transactional
    public void delete(Long id) {
        Review review = reviewRepository.findById(id);
        if (review == null) {
            throw new NotFoundException("Avaliação não encontrada com ID: " + id);
        }
        reviewRepository.delete(review);
    }
    
    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.id = review.id;
        dto.userId = review.user.id;
        dto.skinId = review.skin.id;
        dto.rating = review.rating;
        dto.comment = review.comment;
        dto.reviewDate = review.reviewDate;
        
        // Adicionar informações adicionais para exibição
        dto.userName = review.user.name;
        dto.skinName = review.skin.name;
        
        return dto;
    }
    
    private Review mapToEntity(ReviewDTO dto) {
        Review review = new Review();
        review.user = userRepository.findById(dto.userId);
        review.skin = skinRepository.findById(dto.skinId);
        review.rating = dto.rating;
        review.comment = dto.comment;
        return review;
    }
}