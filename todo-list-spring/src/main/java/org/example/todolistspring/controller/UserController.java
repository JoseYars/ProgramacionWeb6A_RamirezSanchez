package org.example.todolistspring.controller;

import org.example.todolistspring.models.Usuario;
import org.example.todolistspring.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UsuarioRepository usuarioRepository;

    public UserController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser(Authentication authentication) {
        String username = authentication.getName(); // Obtiene el nombre de usuario del contexto de seguridad
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(404).body(null); // Usuario no encontrado
        }
    }
}

