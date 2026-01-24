package com.GardenJava.app.dto.reserva;

import com.GardenJava.app.dto.usuario.UsuarioResumoDTO;
import com.GardenJava.app.model.reserva.OrigemReserva;
import com.GardenJava.app.model.reserva.StatusReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaResponseDTO(

        Long id,

        LocalDateTime dataCheckin,
        LocalDateTime dataCheckout,

        LocalDateTime dataCriacao,

        BigDecimal valorTotalDiarias,

        OrigemReserva origemReserva,
        StatusReserva statusReserva,

        UsuarioResumoDTO cliente,
        UsuarioResumoDTO atendente

) { }
