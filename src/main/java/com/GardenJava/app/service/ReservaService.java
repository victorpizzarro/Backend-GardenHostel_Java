package com.GardenJava.app.service;

import com.GardenJava.app.dto.reserva.ReservaRequestDTO;
import com.GardenJava.app.dto.reserva.ReservaResponseDTO;
import com.GardenJava.app.dto.usuario.UsuarioResumoDTO;
import com.GardenJava.app.model.reserva.Reserva;
import com.GardenJava.app.model.reserva.StatusReserva;
import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;
import com.GardenJava.app.model.vaga.Vaga;
import com.GardenJava.app.repository.ReservaRepository;
import com.GardenJava.app.repository.UsuarioRepository;
import com.GardenJava.app.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VagaRepository vagaRepository;


    public ReservaResponseDTO criar(ReservaRequestDTO dados) {

        Usuario cliente = usuarioRepository.findById(dados.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Usuario atendente = null;
            if (dados.atendenteId() != null) {
                atendente = usuarioRepository.findById(dados.atendenteId())
                        .orElseThrow(() -> new RuntimeException("Atendente não encontrado"));
            }

        Vaga vaga = vagaRepository.findById(dados.vagaId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));


        boolean isOcupada = reservaRepository.isVagaOcupada(
                vaga.getId(),
                dados.dataCheckin(),
                dados.dataCheckout()
        );

        if (isOcupada) {
            throw new RuntimeException("Vaga indisponível para o período selecionado.");
        }


        long dias = ChronoUnit.DAYS.between(dados.dataCheckin(), dados.dataCheckout());
        if (dias < 1) dias = 1; // Garantir 1 diária

        BigDecimal precoDiaria = vaga.getQuarto().getPrecoDiaria();
        BigDecimal valorTotal = precoDiaria.multiply(BigDecimal.valueOf(dias));

        Reserva reserva = new Reserva();

        reserva.setCliente(cliente);
        reserva.setAtendente(atendente);
        reserva.setVaga(vaga);

        reserva.setDataCheckin(dados.dataCheckin());
        reserva.setDataCheckout(dados.dataCheckout());
        reserva.setOrigemReserva(dados.origemReserva());

        reserva.setValorTotalDiarias(valorTotal);

        reserva.setStatusReserva(StatusReserva.PENDENTE);

        reservaRepository.save(reserva);

        return montarResponseDTO(reserva);
    }


    @Transactional(readOnly = true)
    public ReservaResponseDTO buscar(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Usuario logado = usuarioAutenticado();


        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE &&
                !reserva.getCliente().getId().equals(logado.getId())) {
            throw new AccessDeniedException("Acesso negado.");
        }

        return montarResponseDTO(reserva);
    }


    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listar() {

        Usuario logado = usuarioAutenticado();


        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE) {
            return reservaRepository.findByClienteId(logado.getId()).stream()
                    .map(this::montarResponseDTO)
                    .toList();
        }


        return reservaRepository.findAll().stream()
                .map(this::montarResponseDTO)
                .toList();
    }



    public ReservaResponseDTO cancelar(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Usuario logado = usuarioAutenticado();


        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE &&
                !reserva.getCliente().getId().equals(logado.getId())) {
            throw new AccessDeniedException("Você não pode cancelar reservas de terceiros.");
        }



        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE &&
                reserva.getStatusReserva() != StatusReserva.PENDENTE &&
                reserva.getStatusReserva() != StatusReserva.CONFIRMADA) {
            throw new RuntimeException("Não é possível cancelar uma reserva já finalizada ou em andamento.");
        }

        reserva.setStatusReserva(StatusReserva.CANCELADA);


        return montarResponseDTO(reservaRepository.save(reserva));
    }




    public ReservaResponseDTO realizarCheckin(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Usuario logado = usuarioAutenticado();


        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE) {
            if (!reserva.getCliente().getId().equals(logado.getId())) {
                throw new AccessDeniedException("Você não tem permissão para acessar esta reserva.");
            }


            if (LocalDateTime.now().isBefore(reserva.getDataCheckin())) {
                throw new IllegalArgumentException("O Check-in só estará liberado a partir de: " + reserva.getDataCheckin());
            }
        }


        if (reserva.getStatusReserva() == StatusReserva.CANCELADA) {
            throw new RuntimeException("Não é possível fazer check-in em reserva cancelada.");
        }

        if (reserva.getStatusReserva() == StatusReserva.CHECKIN) {
            throw new RuntimeException("O Check-in já foi realizado.");
        }

        reserva.setStatusReserva(StatusReserva.CHECKIN);

        reserva.setDataCheckin(LocalDateTime.now());

        return montarResponseDTO(reservaRepository.save(reserva));
    }



    public ReservaResponseDTO realizarCheckout(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Usuario logado = usuarioAutenticado();


        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE) {

            if (!reserva.getCliente().getId().equals(logado.getId())) {
                throw new AccessDeniedException("Você não tem permissão para acessar esta reserva.");
            }


            if (reserva.getStatusReserva() != StatusReserva.CHECKIN) {
                throw new IllegalArgumentException("Você precisa fazer o check-in antes de finalizar a estadia.");
            }
        }

        reserva.setStatusReserva(StatusReserva.FINALIZADA);
        reserva.setDataCheckout(LocalDateTime.now());

        return montarResponseDTO(reservaRepository.save(reserva));
    }


    public void deletar(Long id) {

        Usuario logado = usuarioAutenticado();
        if (logado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new AccessDeniedException("Apenas Administradores podem deletar registros de reserva.");
        }
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva não encontrada");
        }
        reservaRepository.deleteById(id);
    }



    private ReservaResponseDTO montarResponseDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getDataCriacao(),
                reserva.getDataCheckin(),
                reserva.getDataCheckout(),
                reserva.getValorTotalDiarias(),
                reserva.getOrigemReserva(),
                reserva.getStatusReserva(),
                UsuarioResumoDTO.from(reserva.getCliente()),
                UsuarioResumoDTO.from(reserva.getAtendente())
        );
    }

    private Usuario usuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }
}