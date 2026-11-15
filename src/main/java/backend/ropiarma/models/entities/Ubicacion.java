package backend.ropiarma.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Data 
@Table (name = "ubicacion")
public class Ubicacion {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUbicacion;

    @Column (nullable = false)
    private String region, comuna, direccion;

    @Column (nullable = false)
    private double costoEnvio;

    @ManyToOne 
    @JoinColumn (name = "idUsuario", nullable = false)
    private Usuario usuario;
    
}
