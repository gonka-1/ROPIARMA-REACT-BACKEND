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
@Table (name = "producto")
public class Producto {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProducto;

    @Column (nullable = false)
    private String nombre, descripcion;

    @Column (nullable = false)
    private int precio, stock;

    @ManyToOne 
    @JoinColumn (name = "idCategoria", nullable = false)
    private Categoria categoria;

    
}
