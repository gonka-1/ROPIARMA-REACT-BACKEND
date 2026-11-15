package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

import backend.ropiarma.models.entities.DetalleCarrito;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

    List<DetalleCarrito> findByCarritoIdCarrito(int idCarrito);

    Optional<DetalleCarrito> findByCarritoIdCarritoAndProductoIdProducto(int idCarrito, int idProducto);

    void deleteByCarritoIdCarrito(int idCarrito);
    
}
