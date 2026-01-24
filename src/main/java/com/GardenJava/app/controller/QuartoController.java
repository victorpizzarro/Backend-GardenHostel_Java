package com.GardenJava.app.controller;

import com.GardenJava.app.dto.quarto.QuartoRequestDTO;
import com.GardenJava.app.dto.quarto.QuartoResponseDTO;
import com.GardenJava.app.service.QuartoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quartos")
@RequiredArgsConstructor
public class QuartoController {

    private final QuartoService service;

    @GetMapping
    public List<QuartoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public QuartoResponseDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE')")
    @PostMapping
    public ResponseEntity<QuartoResponseDTO> criar(@RequestBody @Valid QuartoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE')")
    @PutMapping("/{id}")
    public QuartoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid QuartoRequestDTO dto) {
        return service.editar(id, dto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}