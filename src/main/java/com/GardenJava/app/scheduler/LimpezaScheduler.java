package com.GardenJava.app.scheduler;

import com.GardenJava.app.model.reserva.Reserva;
import com.GardenJava.app.model.vaga.StatusVaga;
import com.GardenJava.app.model.vaga.Vaga;
import com.GardenJava.app.repository.ReservaRepository;
import com.GardenJava.app.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Para logs bonitos
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LimpezaScheduler {

    private final VagaRepository vagaRepository;
    private final ReservaRepository reservaRepository;

    private static final int TEMPO_LIMPEZA_MINUTOS = 60;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void liberarVagasLimpasAutomaticamente() {

        List<Vaga> vagasEmLimpeza = vagaRepository.findAllByStatus(StatusVaga.LIMPEZA);

        if (vagasEmLimpeza.isEmpty()) return;

        LocalDateTime agora = LocalDateTime.now();

        for (Vaga vaga : vagasEmLimpeza) {

            Reserva ultimaReserva = reservaRepository.findTopByVagaIdOrderByDataCheckoutDesc(vaga.getId());

            if (ultimaReserva != null && ultimaReserva.getDataCheckout() != null) {


                LocalDateTime horarioLiberacao = ultimaReserva.getDataCheckout().plusMinutes(TEMPO_LIMPEZA_MINUTOS);


                if (agora.isAfter(horarioLiberacao)) {
                    vaga.setStatus(StatusVaga.LIVRE);
                    vagaRepository.save(vaga);

                    log.info("VAGA LIBERADA AUTOMATICAMENTE: ID {} (Estava em limpeza desde {})",
                            vaga.getId(), ultimaReserva.getDataCheckout());
                }
            }
        }
    }
}