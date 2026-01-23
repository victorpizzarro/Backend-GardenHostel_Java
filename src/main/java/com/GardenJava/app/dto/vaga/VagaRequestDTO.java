package com.GardenJava.app.dto.vaga;


import com.GardenJava.app.model.vaga.StatusVaga;
import jakarta.validation.constraints.NotBlank;

public record VagaRequestDTO(
        @NotBlank(message = "O identificador da vaga é obrigatório (ex: Cama 1)")
        String nomeIdentificador,

        String descricaoPeculiaridadesPt,
        String descricaoPeculiaridadesEn,


        StatusVaga status
) { }