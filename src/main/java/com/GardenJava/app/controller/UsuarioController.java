package com.GardenJava.app.controller;

import com.GardenJava.app.dto.usuario.UsuarioRequestDTO;
import com.GardenJava.app.dto.usuario.UsuarioResponseDTO;
import com.GardenJava.app.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    @GetMapping("/{id}")
    public UsuarioResponseDTO buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return service.listar();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    @PutMapping("/{id}/perfil")
    public UsuarioResponseDTO editarPerfil(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioRequestDTO body
    ) {
        return service.editar(id, body);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    @PutMapping("/{id}/email")
    public UsuarioResponseDTO alterarEmail(@PathVariable UUID id, @RequestBody String novoEmail) {
        return service.alterarEmail(id, novoEmail);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    @PutMapping("/{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable UUID id, @RequestBody String novaSenha) {
        service.alterarSenha(id, novaSenha);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}