package com.GardenJava.app.service;

import com.GardenJava.app.dto.quarto.QuartoRequestDTO;
import com.GardenJava.app.dto.quarto.QuartoResponseDTO;
import com.GardenJava.app.model.quarto.Quarto;
import com.GardenJava.app.repository.QuartoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuartoService {

    private final QuartoRepository repository;

    public List<QuartoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public QuartoResponseDTO criar(QuartoRequestDTO body) {
        Quarto quarto = new Quarto();
        quarto.atualizarDados(
                body.nome(),
                body.descricaoPt(),
                body.descricaoEn(),
                body.tipoQuarto(),
                body.capacidade(),
                body.banheiroPrivativo(),
                body.precoDiaria()
        );

        return toResponseDTO(repository.save(quarto));
    }

    public QuartoResponseDTO buscar(Long id) {
        Quarto quarto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quarto não encontrado"));
        return toResponseDTO(quarto);
    }

    public QuartoResponseDTO editar(Long id, QuartoRequestDTO body) {
        Quarto quarto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quarto não encontrado"));

        quarto.atualizarDados(
                body.nome(),
                body.descricaoPt(),
                body.descricaoEn(),
                body.tipoQuarto(),
                body.capacidade(),
                body.banheiroPrivativo(),
                body.precoDiaria()
        );
        return toResponseDTO(repository.save(quarto));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Quarto não encontrado");
        }
        repository.deleteById(id);
    }

    private QuartoResponseDTO toResponseDTO(Quarto quarto) {
        List<Long> vagasIds = quarto.getVagas() == null ? List.of() :
                quarto.getVagas().stream().map(v -> v.getId()).toList();

        return new QuartoResponseDTO(
                quarto.getId(),
                quarto.getNome(),
                quarto.getDescricaoPt(),
                quarto.getDescricaoEn(),
                quarto.getTipoQuarto(),
                quarto.getCapacidade(),
                quarto.isBanheiroPrivativo(),
                quarto.getPrecoDiaria(),
                vagasIds
        );
    }
}