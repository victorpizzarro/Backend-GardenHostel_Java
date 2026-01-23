package com.GardenJava.app.dto.quarto;

import com.GardenJava.app.model.quarto.TipoQuarto;

import java.math.BigDecimal;
import java.util.List;

public record QuartoResponseDTO(
        Long id,
        String nome,
        String descricaoPt,
        String descricaoEn,
        TipoQuarto tipoQuarto,
        int capacidade,
        boolean banheiroPrivativo,
        BigDecimal precoDiaria,

        List<Long> vagasIds
) { }