package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
