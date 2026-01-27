package com.GardenJava.app.dto.reserva;

import com.GardenJava.app.dto.usuario.UsuarioResumoDTO;
import com.GardenJava.app.model.reserva.OrigemReserva;
import com.GardenJava.app.model.reserva.Reserva;
import com.GardenJava.app.model.reserva.StatusReserva;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaResponseDTO(
        Long id,
        LocalDateTime dataCheckin,
        LocalDateTime dataCheckout,
        LocalDateTime dataCriacao,
        BigDecimal valorTotal,
        OrigemReserva origem,
        StatusReserva status,

        UsuarioResumoDTO cliente,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        UsuarioResumoDTO atendente
) {

        public static ReservaResponseDTO from(Reserva reserva) {
                if (reserva == null) return null;

                return new ReservaResponseDTO(
                        reserva.getId(),
                        reserva.getDataCheckin(),
                        reserva.getDataCheckout(),
                        reserva.getDataCriacao(),
                        reserva.getValorTotalDiarias(),
                        reserva.getOrigemReserva(),
                        reserva.getStatusReserva(),

                        UsuarioResumoDTO.from(reserva.getCliente()),

                        UsuarioResumoDTO.from(reserva.getAtendente())
                );
        }
}