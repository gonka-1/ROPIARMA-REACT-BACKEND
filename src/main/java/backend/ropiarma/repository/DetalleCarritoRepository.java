package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.DetalleCarrito;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {
    
}
