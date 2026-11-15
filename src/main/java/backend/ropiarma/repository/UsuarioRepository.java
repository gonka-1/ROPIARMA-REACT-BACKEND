package backend.ropiarma.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :username")
    boolean existsByUsername(@Param("username") String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

}