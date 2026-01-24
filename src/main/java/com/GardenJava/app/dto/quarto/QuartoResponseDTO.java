package com.GardenJava.app.dto.quarto;

import com.GardenJava.app.model.quarto.TipoQuarto;
import com.GardenJava.app.model.quarto.Quarto;
import com.GardenJava.app.model.vaga.Vaga;

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
) {
    public static QuartoResponseDTO from(Quarto entity) {
        if (entity == null) return null;

        
        List<Long> vagasIds = entity.getVagas() == null ? List.of() :
                entity.getVagas().stream().map(Vaga::getId).toList();

        return new QuartoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricaoPt(),
                entity.getDescricaoEn(),
                entity.getTipoQuarto(),
                entity.getCapacidade(),
                entity.isBanheiroPrivativo(),
                entity.getPrecoDiaria(),
                vagasIds
        );
    }
}