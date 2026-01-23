package com.GardenJava.app.service;

import com.GardenJava.app.dto.vaga.VagaRequestDTO;
import com.GardenJava.app.dto.vaga.VagaResponseDTO;

import com.GardenJava.app.model.quarto.Quarto;
import com.GardenJava.app.model.vaga.Vaga;
import com.GardenJava.app.repository.QuartoRepository;
import com.GardenJava.app.repository.VagaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;
    private final QuartoRepository quartoRepository;

    public VagaResponseDTO criar(Long quartoId, VagaRequestDTO body) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new EntityNotFoundException("Quarto não encontrado"));


        long vagasExistentes = vagaRepository.countByQuartoId(quartoId);
        if (vagasExistentes >= quarto.getCapacidade()) {
            throw new IllegalArgumentException("Não é possível adicionar a vaga. A capacidade máxima do quarto (" + quarto.getCapacidade() + ") foi atingida.");
        }


        Vaga vaga = new Vaga();
        vaga.setNomeIdentificador(body.nomeIdentificador());
        vaga.setDescricaoPeculiaridadesPt(body.descricaoPeculiaridadesPt());
        vaga.setDescricaoPeculiaridadesEn(body.descricaoPeculiaridadesEn());
        vaga.setQuarto(quarto);


        if (body.status() != null) {
            vaga.setStatus(body.status());
        }

        Vaga salva = vagaRepository.save(vaga);
        return toResponseDTO(salva);
    }

    public List<VagaResponseDTO> listarPorQuarto(Long quartoId) {

        if (!quartoRepository.existsById(quartoId)) {
            throw new EntityNotFoundException("Quarto não encontrado");
        }
        return vagaRepository.findByQuartoId(quartoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }



    public VagaResponseDTO buscar(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));
        return toResponseDTO(vaga);
    }



    public VagaResponseDTO editar(Long id, VagaRequestDTO body) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        vaga.setNomeIdentificador(body.nomeIdentificador());
        vaga.setDescricaoPeculiaridadesPt(body.descricaoPeculiaridadesPt());
        vaga.setDescricaoPeculiaridadesEn(body.descricaoPeculiaridadesEn());

        if (body.status() != null) {
            vaga.setStatus(body.status());
        }

        return toResponseDTO(vagaRepository.save(vaga));
    }



    public void deletar(Long id) {
        if (!vagaRepository.existsById(id)) {
            throw new EntityNotFoundException("Vaga não encontrada");
        }
        vagaRepository.deleteById(id);
    }


    private VagaResponseDTO toResponseDTO(Vaga vaga) {
        return new VagaResponseDTO(
                vaga.getId(),
                vaga.getNomeIdentificador(),
                vaga.getDescricaoPeculiaridadesPt(),
                vaga.getDescricaoPeculiaridadesEn(),
                vaga.getStatus(),
                vaga.getQuarto().getId()
        );
    }
}