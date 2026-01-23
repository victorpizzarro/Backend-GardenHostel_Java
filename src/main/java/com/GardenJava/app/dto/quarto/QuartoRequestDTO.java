package com.GardenJava.app.dto.quarto;

import com.GardenJava.app.model.quarto.TipoQuarto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record QuartoRequestDTO(
        @NotBlank(message = "O nome do quarto é obrigatório")
        String nome,

        String descricaoPt,
        String descricaoEn,

        @NotNull(message = "O tipo do quarto é obrigatório")
        TipoQuarto tipoQuarto,

        @Min(value = 1, message = "A capacidade deve ser de pelo menos 1 pessoa")
        int capacidade,

        boolean banheiroPrivativo,

        @NotNull(message = "O preço da diária é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal precoDiaria

) { }