package com.GardenJava.app.repository;

import com.GardenJava.app.model.reserva.Reserva;
import com.GardenJava.app.model.reserva.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    List<Reserva> findByClienteId(UUID clienteId);


    List<Reserva> findByStatusReserva(StatusReserva status);


    @Query("""
           SELECT COUNT(r) > 0 
           FROM Reserva r 
           WHERE r.vaga.id = :vagaId 
           AND r.statusReserva <> 'CANCELADA'
           AND (r.dataCheckin < :dataCheckout AND r.dataCheckout > :dataCheckin)
           """)

    boolean isVagaOcupada(@Param("vagaId") Long vagaId,
                          @Param("dataCheckin") LocalDateTime dataCheckin,
                          @Param("dataCheckout") LocalDateTime dataCheckout);
}