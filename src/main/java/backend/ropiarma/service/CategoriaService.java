package backend.ropiarma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.Categoria;
import backend.ropiarma.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    // devuelve null si no existe
    public Categoria buscarPorId(int id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // devuelve null si ya existe una categoria con ese nombre
    public Categoria guardar(Categoria categoria) {
        if (categoriaRepository.existsByNombreCategoriaIgnoreCase(categoria.getNombreCategoria())) {
            return null;
        }
        return categoriaRepository.save(categoria);
    }

    // devuelve null si no existe
    public Categoria actualizar(int id, Categoria datos) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            return null;
        }
        categoria.setNombreCategoria(datos.getNombreCategoria());
        return categoriaRepository.save(categoria);
    }

    // devuelve false si no existe
    public boolean eliminar(int id) {
        if (!categoriaRepository.existsById(id)) {
            return false;
        }
        categoriaRepository.deleteById(id);
        return true;
    }
}
