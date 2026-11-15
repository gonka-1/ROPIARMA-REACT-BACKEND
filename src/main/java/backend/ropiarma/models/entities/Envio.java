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
@Table (name = "envio")
public class Envio {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEnvio;

    @Column (nullable = false)
    private String codigoSeguimiento, estadoEnvio;

    @Column (nullable = false)
    private LocalDate fechaActualizacion;

    @ManyToOne 
    @JoinColumn (name = "idBoleta", nullable = false)
    private Boleta boleta;


    
}
