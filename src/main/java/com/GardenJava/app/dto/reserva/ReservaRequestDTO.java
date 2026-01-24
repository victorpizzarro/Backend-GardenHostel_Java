package com.GardenJava.app.dto.reserva;

import com.GardenJava.app.model.reserva.OrigemReserva;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaRequestDTO(

        @NotNull(message = "A data de check-in é obrigatória")
        @FutureOrPresent(message = "A data de check-in não pode ser no passado")
         LocalDateTime dataCheckin,

        @NotNull(message = "A data de check-out é obrigatória")
         @Future(message = "A data de check-out deve ser futura")
         LocalDateTime dataCheckout,


         @NotNull(message = "A origem da reserva é obrigatória")
         OrigemReserva origemReserva,

        @NotNull(message = "O ID do cliente é obrigatório")
         UUID clienteId,

         UUID atendenteId,

        @NotNull(message = "O ID da vaga é obrigatório")
        Long vagaId

        ) { }
