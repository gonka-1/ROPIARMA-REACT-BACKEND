package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.Carrito;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    
}
