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
@Table (name = "pago")
public class Pago {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPago;

    @Column (nullable = false)
    private double monto;

    @Column (nullable = false)
    private String metodoPago;

    @Column (nullable = false)
    private String estado;

    @Column (nullable = false)
    private LocalDate fechaPago;

    @ManyToOne 
    @JoinColumn (name = "idBoleta", nullable = false)
    private Boleta boleta;
    
}
