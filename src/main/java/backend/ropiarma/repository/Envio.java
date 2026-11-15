package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface Envio extends JpaRepository<Envio, Integer> {
    
}
