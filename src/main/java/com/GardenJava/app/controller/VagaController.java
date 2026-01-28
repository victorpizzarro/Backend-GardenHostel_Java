package com.GardenJava.app.controller;

import com.GardenJava.app.dto.vaga.VagaRequestDTO;
import com.GardenJava.app.dto.vaga.VagaResponseDTO;
import com.GardenJava.app.service.VagaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VagaController {

    private final VagaService service;



    @GetMapping("/api/quartos/{quartoId}/vagas")
    public List<VagaResponseDTO> listarPorQuarto(@PathVariable Long quartoId) {
        return service.listarPorQuarto(quartoId);
    }


    @GetMapping("/api/vagas/{id}")
    public VagaResponseDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE')")
    @PostMapping("/api/quartos/{quartoId}/vagas")
    public ResponseEntity<VagaResponseDTO> criar(
            @PathVariable Long quartoId,
            @RequestBody @Valid VagaRequestDTO body
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(quartoId, body));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE')")
    @PutMapping("/api/vagas/{id}")
    public VagaResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid VagaRequestDTO body
    ) {
        return service.editar(id, body);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE')")
    @DeleteMapping("/api/vagas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE')")
    @PatchMapping("/api/vagas/{id}/liberar")
    public ResponseEntity<VagaResponseDTO> liberarLimpeza(@PathVariable Long id) {

        VagaResponseDTO vagaAtualizada = service.liberarLimpezaManualmente(id);
        return ResponseEntity.ok(vagaAtualizada);
    }
}