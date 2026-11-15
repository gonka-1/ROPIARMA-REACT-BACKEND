package backend.ropiarma.models.entities;

import java.time.LocalDate;

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
@Table (name = "resena")
public class Resena {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int idResena;

    @Column (nullable = false)
    private int calificacion;

    @Column (nullable = false)
    private String comentario;

    @Column (nullable = false)
    private LocalDate fecha;

    @ManyToOne 
    @JoinColumn (name = "idUsuario", nullable = false)
    private Usuario usuario;
    
}
