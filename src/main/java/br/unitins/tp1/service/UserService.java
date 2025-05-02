package br.unitins.tp1.service;

import br.unitins.tp1.dto.UserDTO;
import br.unitins.tp1.model.User;
import br.unitins.tp1.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {
    
    @Inject
    UserRepository userRepository;
    
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + id);
        }
        return mapToDTO(user);
    }
    
    @Transactional
    public UserDTO create(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        userRepository.persist(user);
        return mapToDTO(user);
    }
    
    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + id);
        }
        
        user.name = userDTO.name;
        user.email = userDTO.email;
        if (userDTO.password != null && !userDTO.password.isEmpty()) {
            user.password = userDTO.password; // Em produção, deve-se criptografar a senha
        }
        user.address = userDTO.address;
        user.phone = userDTO.phone;
        
        return mapToDTO(user);
    }
    
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + id);
        }
        userRepository.delete(user);
    }
    
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.id;
        dto.name = user.name;
        dto.email = user.email;
        dto.address = user.address;
        dto.phone = user.phone;
        // Não incluímos a senha no DTO por segurança
        return dto;
    }
    
    private User mapToEntity(UserDTO dto) {
        User user = new User();
        user.name = dto.name;
        user.email = dto.email;
        user.password = dto.password; // Em produção, deve-se criptografar a senha
        user.address = dto.address;
        user.phone = dto.phone;
        return user;
    }
}