package com.GardenJava.app.dto.pagamento;

import com.GardenJava.app.model.pagamento.Pagamento;
import com.GardenJava.app.model.pagamento.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
        Long id,
        StatusPagamento status,
        BigDecimal valor,
        LocalDateTime data,
        Long reservaId
) {
    public static PagamentoResponseDTO from(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getStatus(),
                pagamento.getValor(),
                pagamento.getDataPagamento(),
                pagamento.getReserva().getId()
        );
    }
}