package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import backend.ropiarma.models.entities.Boleta;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface BoletaRepository extends JpaRepository<Boleta, Integer> {

    List<Boleta> findByUsuarioIdUsuario(int idUsuario);
    
}
