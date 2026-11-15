package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import backend.ropiarma.models.entities.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoriaIdCategoria(int idCategoria);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}
