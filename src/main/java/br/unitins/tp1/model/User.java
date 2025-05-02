package br.unitins.tp1.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "name", nullable = false)
    public String name;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(name = "email", unique = true, nullable = false)
    public String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "password", nullable = false)
    public String password;
    
    @Column(name = "address")
    public String address;
    
    @Column(name = "phone")
    public String phone;
}