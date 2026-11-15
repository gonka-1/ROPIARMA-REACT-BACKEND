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
@Table (name = "detalle_carrito")
public class DetalleCarrito {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetalleCarrito;

    @Column (nullable = false)
    private int cantidad;

    @Column (nullable = false)
    private double precioUnitario;

    @ManyToOne
    @JoinColumn (name = "idProducto", nullable = false)
    private Producto producto;
    
    @ManyToOne
    @JoinColumn (name = "idCarrito", nullable = false)
    private Carrito carrito;
    
}
