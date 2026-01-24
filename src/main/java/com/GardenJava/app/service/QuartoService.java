package com.GardenJava.app.service;

import com.GardenJava.app.dto.quarto.QuartoRequestDTO;
import com.GardenJava.app.dto.quarto.QuartoResponseDTO;
import com.GardenJava.app.model.quarto.Quarto;
import com.GardenJava.app.repository.QuartoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuartoService {

    private final QuartoRepository repository;

    @Transactional(readOnly = true)
    public List<QuartoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(QuartoResponseDTO::from)
                .toList();
    }

    public QuartoResponseDTO criar(QuartoRequestDTO body) {
        Quarto quarto = new Quarto();
        atualizarDadosQuarto(quarto, body);
        return QuartoResponseDTO.from(repository.save(quarto));
    }

    @Transactional(readOnly = true)
    public QuartoResponseDTO buscar(Long id) {
        Quarto quarto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quarto não encontrado"));
        return QuartoResponseDTO.from(quarto);
    }

    public QuartoResponseDTO editar(Long id, QuartoRequestDTO body) {
        Quarto quarto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quarto não encontrado"));

        atualizarDadosQuarto(quarto, body);
        return QuartoResponseDTO.from(repository.save(quarto));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Quarto não encontrado");
        }
        repository.deleteById(id);
    }


    private void atualizarDadosQuarto(Quarto quarto, QuartoRequestDTO body) {
        quarto.atualizarDados(
                body.nome(),
                body.descricaoPt(),
                body.descricaoEn(),
                body.tipoQuarto(),
                body.capacidade(),
                body.banheiroPrivativo(),
                body.precoDiaria()
        );
    }
}