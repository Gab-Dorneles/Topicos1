package br.unitins.tp1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    
    public Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    public String name;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    public String email;
    
    public String password;
    
    public String address;
    
    public String phone;
}