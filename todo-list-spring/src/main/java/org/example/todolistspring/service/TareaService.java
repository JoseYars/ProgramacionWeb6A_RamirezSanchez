package org.example.todolistspring.service;

import org.example.todolistspring.models.Tarea;
import org.example.todolistspring.repository.TareaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea guardar(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public Tarea actualizar(Long id, Tarea tareaActualizada) {
        return tareaRepository.findById(id).map( tarea -> {
            tarea.setTitulo(tareaActualizada.getTitulo());
            tarea.setDescripcion(tareaActualizada.getDescripcion());
            tarea.setCompletada(tareaActualizada.isCompletada());
            return tareaRepository.save(tarea);
        }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    public Tarea actualizarParcial(Long id, Map<String, Object> campos) {
        return tareaRepository.findById(id).map(tarea -> {
            if (campos.containsKey("titulo")) {
                tarea.setTitulo((String) campos.get("titulo"));
            }
            if (campos.containsKey("descripcion")) {
                tarea.setDescripcion((String) campos.get("descripcion"));
            }
            if (campos.containsKey("completada")) {
                tarea.setCompletada((Boolean) campos.get("completada"));
            }
            return tareaRepository.save(tarea);
        }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

    }

    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }



}




