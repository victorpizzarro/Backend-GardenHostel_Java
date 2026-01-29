package com.GardenJava.app.dto.avaliacao;

import com.GardenJava.app.model.avaliacao.Avaliacao;
import java.time.LocalDateTime;

public record AvaliacaoResponseDTO(

        Long id,
        Integer nota,
        String comentario,
        LocalDateTime data,
        String nomeCliente,
        Long quartoId
) {

    public static AvaliacaoResponseDTO from(Avaliacao entity) {
        return new AvaliacaoResponseDTO(
                entity.getId(),
                entity.getNota(),
                entity.getComentario(),
                entity.getDataAvaliacao(),
                entity.getReserva().getCliente().getNomeCompleto(),
                entity.getReserva().getVaga().getQuarto().getId()
        );
    }
}