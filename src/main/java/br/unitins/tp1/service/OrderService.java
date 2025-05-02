package br.unitins.tp1.service;

import br.unitins.tp1.dto.OrderDTO;
import br.unitins.tp1.dto.OrderItemDTO;
import br.unitins.tp1.model.Order;
import br.unitins.tp1.model.OrderItem;
import br.unitins.tp1.model.Skin;
import br.unitins.tp1.model.User;
import br.unitins.tp1.repository.OrderRepository;
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
public class OrderService {
    
    @Inject
    OrderRepository orderRepository;
    
    @Inject
    UserRepository userRepository;
    
    @Inject
    SkinRepository skinRepository;
    
    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> findByUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + userId);
        }
        return orderRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new NotFoundException("Pedido não encontrado com ID: " + id);
        }
        return mapToDTO(order);
    }
    
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        Order order = new Order();
        
        User user = userRepository.findById(orderDTO.userId);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado com ID: " + orderDTO.userId);
        }
        
        order.user = user;
        order.orderDate = LocalDateTime.now();
        order.status = "PENDING";
        
        orderRepository.persist(order);
        
        // Adicionar itens ao pedido
        for (OrderItemDTO itemDTO : orderDTO.items) {
            Skin skin = skinRepository.findById(itemDTO.skinId);
            if (skin == null) {
                throw new NotFoundException("Skin não encontrada com ID: " + itemDTO.skinId);
            }
            
            OrderItem item = new OrderItem();
            item.order = order;
            item.skin = skin;
            item.quantity = itemDTO.quantity;
            item.price = skin.price;
            
            order.items.add(item);
        }
        
        // Calcular o total do pedido
        order.calculateTotal();
        
        return mapToDTO(order);
    }
    
    @Transactional
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new NotFoundException("Pedido não encontrado com ID: " + id);
        }
        
        order.status = orderDTO.status;
        
        return mapToDTO(order);
    }
    
    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new NotFoundException("Pedido não encontrado com ID: " + id);
        }
        orderRepository.delete(order);
    }
    
    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.id;
        dto.userId = order.user.id;
        dto.orderDate = order.orderDate;
        dto.totalAmount = order.totalAmount;
        dto.status = order.status;
        
        dto.items = order.items.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.id = item.id;
            itemDTO.orderId = order.id;
            itemDTO.skinId = item.skin.id;
            itemDTO.quantity = item.quantity;
            itemDTO.price = item.price;
            return itemDTO;
        }).collect(Collectors.toList());
        
        return dto;
    }
}