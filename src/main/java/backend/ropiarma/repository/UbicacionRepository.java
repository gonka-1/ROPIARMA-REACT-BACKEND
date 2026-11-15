package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import backend.ropiarma.models.entities.Ubicacion;
import backend.ropiarma.models.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository 
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :username")
    boolean existsByUsername(@Param("username") String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
    
}
