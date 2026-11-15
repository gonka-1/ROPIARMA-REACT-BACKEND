package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
}
