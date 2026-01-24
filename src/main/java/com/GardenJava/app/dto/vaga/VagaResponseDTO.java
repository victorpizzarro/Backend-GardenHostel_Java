package com.GardenJava.app.dto.vaga;

import com.GardenJava.app.model.vaga.StatusVaga;
import com.GardenJava.app.model.vaga.Vaga;

public record VagaResponseDTO(
        Long id,
        String nomeIdentificador,
        String descricaoPeculiaridadesPt,
        String descricaoPeculiaridadesEn,
        StatusVaga status,
        Long quartoId
) {
    public static VagaResponseDTO from(Vaga entity) {
        if (entity == null) return null;

        return new VagaResponseDTO(
                entity.getId(),
                entity.getNomeIdentificador(),
                entity.getDescricaoPeculiaridadesPt(),
                entity.getDescricaoPeculiaridadesEn(),
                entity.getStatus(),


                entity.getQuarto() != null ? entity.getQuarto().getId() : null
        );
    }
}