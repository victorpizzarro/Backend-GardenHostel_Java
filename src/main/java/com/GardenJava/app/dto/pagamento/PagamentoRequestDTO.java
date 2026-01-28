package com.GardenJava.app.dto.pagamento;

import com.GardenJava.app.model.pagamento.FormaPagamento;
import com.GardenJava.app.model.pagamento.OrigemPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDTO(
        @NotNull(message = "O ID da reserva é obrigatório")
        Long reservaId,

        @NotNull(message = "A forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        @NotNull
        OrigemPagamento origem
) {}