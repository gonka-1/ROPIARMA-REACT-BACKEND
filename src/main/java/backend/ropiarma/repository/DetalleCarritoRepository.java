package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public class DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {
    
}
