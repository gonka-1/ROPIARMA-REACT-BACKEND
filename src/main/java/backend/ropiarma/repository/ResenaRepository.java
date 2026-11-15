package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.Resena;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface  ResenaRepository extends JpaRepository<Resena, Integer> {
    
}
