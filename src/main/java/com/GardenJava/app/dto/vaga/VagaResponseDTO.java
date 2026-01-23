package com.GardenJava.app.dto.vaga;


import com.GardenJava.app.model.vaga.StatusVaga;

public record VagaResponseDTO(
        Long id,
        String nomeIdentificador,
        String descricaoPeculiaridadesPt,
        String descricaoPeculiaridadesEn,
        StatusVaga status,
        Long quartoId
) { }