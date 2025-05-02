package br.unitins.tp1.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "champions")
public class Champion extends PanacheEntity {
    
    @NotBlank(message = "Nome do campeão é obrigatório")
    @Column(name = "name", nullable = false, unique = true)
    public String name;
    
    @Column(name = "role")
    public String role;
    
    @Column(name = "description", length = 2000)
    public String description;
    
    @Column(name = "image_url")
    public String imageUrl;
    
    @OneToMany(mappedBy = "champion", fetch = FetchType.LAZY)
    public List<Skin> skins = new ArrayList<>();
}
