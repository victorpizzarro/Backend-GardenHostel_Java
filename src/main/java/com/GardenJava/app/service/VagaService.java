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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VagaService {

    private final VagaRepository vagaRepository;
    private final QuartoRepository quartoRepository;

    public VagaResponseDTO criar(Long quartoId, VagaRequestDTO body) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new EntityNotFoundException("Quarto não encontrado"));


        long vagasExistentes = vagaRepository.countByQuartoId(quartoId);
        if (vagasExistentes >= quarto.getCapacidade()) {
            throw new IllegalArgumentException("Capacidade máxima do quarto atingida (" + quarto.getCapacidade() + ").");
        }

        Vaga vaga = new Vaga();
        vaga.setNomeIdentificador(body.nomeIdentificador());
        vaga.setDescricaoPeculiaridadesPt(body.descricaoPeculiaridadesPt());
        vaga.setDescricaoPeculiaridadesEn(body.descricaoPeculiaridadesEn());
        vaga.setQuarto(quarto);

        if (body.status() != null) {
            vaga.setStatus(body.status());
        }

        return VagaResponseDTO.from(vagaRepository.save(vaga));
    }

    @Transactional(readOnly = true)
    public List<VagaResponseDTO> listarPorQuarto(Long quartoId) {
        if (!quartoRepository.existsById(quartoId)) {
            throw new EntityNotFoundException("Quarto não encontrado");
        }
        return vagaRepository.findByQuartoId(quartoId).stream()
                .map(VagaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VagaResponseDTO buscar(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));
        return VagaResponseDTO.from(vaga);
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
        return VagaResponseDTO.from(vagaRepository.save(vaga));
    }

    public void deletar(Long id) {
        if (!vagaRepository.existsById(id)) {
            throw new EntityNotFoundException("Vaga não encontrada");
        }
        vagaRepository.deleteById(id);
    }
}