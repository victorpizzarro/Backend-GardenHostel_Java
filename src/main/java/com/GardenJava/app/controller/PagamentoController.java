package com.GardenJava.app.controller;

import com.GardenJava.app.dto.pagamento.PagamentoRequestDTO;
import com.GardenJava.app.dto.pagamento.PagamentoResponseDTO;
import com.GardenJava.app.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<PagamentoResponseDTO> confirmarPagamento(@RequestBody @Valid PagamentoRequestDTO dados) {
        var pagamento = service.confirmarPagamento(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ATENDENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<PagamentoResponseDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }
}