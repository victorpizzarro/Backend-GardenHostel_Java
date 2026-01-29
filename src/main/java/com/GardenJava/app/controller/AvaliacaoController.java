package com.GardenJava.app.controller;

import com.GardenJava.app.dto.avaliacao.AvaliacaoRequestDTO;
import com.GardenJava.app.dto.avaliacao.AvaliacaoResponseDTO;
import com.GardenJava.app.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<AvaliacaoResponseDTO> criar(@RequestBody @Valid AvaliacaoRequestDTO dados) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.avaliar(dados));
    }

    @GetMapping("/quarto/{quartoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorQuarto(@PathVariable Long quartoId) {
        return ResponseEntity.ok(service.listarPorQuarto(quartoId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<AvaliacaoResponseDTO> editar(
            @PathVariable Long id,
            @RequestBody @Valid AvaliacaoRequestDTO dados
    ) {
        return ResponseEntity.ok(service.editar(id, dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENTE', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}