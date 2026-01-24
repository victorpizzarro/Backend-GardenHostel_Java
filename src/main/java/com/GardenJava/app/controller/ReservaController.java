package com.GardenJava.app.controller;

import com.GardenJava.app.dto.reserva.ReservaRequestDTO;
import com.GardenJava.app.dto.reserva.ReservaResponseDTO;

import com.GardenJava.app.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }



    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> criar(@RequestBody @Valid ReservaRequestDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dados));
    }


    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }


    @PatchMapping("/{id}/checkin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> checkin(@PathVariable Long id) {
        return ResponseEntity.ok(service.realizarCheckin(id));
    }


    @PatchMapping("/{id}/checkout")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> checkout(@PathVariable Long id) {
        return ResponseEntity.ok(service.realizarCheckout(id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}