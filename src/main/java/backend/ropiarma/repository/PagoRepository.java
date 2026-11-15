package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public class PagoRepository extends JpaRepository<Pago, Integer> {
    
}
