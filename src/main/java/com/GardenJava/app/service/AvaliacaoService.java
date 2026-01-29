package com.GardenJava.app.service;

import com.GardenJava.app.dto.avaliacao.AvaliacaoRequestDTO;
import com.GardenJava.app.dto.avaliacao.AvaliacaoResponseDTO;
import com.GardenJava.app.infra.security.SecurityService;
import com.GardenJava.app.model.avaliacao.Avaliacao;
import com.GardenJava.app.model.reserva.Reserva;
import com.GardenJava.app.model.reserva.StatusReserva;
import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;
import com.GardenJava.app.repository.AvaliacaoRepository;
import com.GardenJava.app.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ReservaRepository reservaRepository;
    private final SecurityService securityService;


    public AvaliacaoResponseDTO avaliar(AvaliacaoRequestDTO dados) {
        Reserva reserva = reservaRepository.findById(dados.reservaId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Usuario usuarioLogado = securityService.getUsuarioLogado();


        if (!reserva.getCliente().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você só pode avaliar suas próprias reservas.");
        }


        if (reserva.getStatusReserva() != StatusReserva.FINALIZADA) {
            throw new RuntimeException("Você só pode avaliar após realizar o Check-out.");
        }


        if (avaliacaoRepository.existsByReservaId(reserva.getId())) {
            throw new RuntimeException("Esta reserva já foi avaliada.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dados.nota());
        avaliacao.setComentario(dados.comentario());
        avaliacao.setReserva(reserva);

        return AvaliacaoResponseDTO.from(avaliacaoRepository.save(avaliacao));
    }


    public AvaliacaoResponseDTO editar(Long id, AvaliacaoRequestDTO dados) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Usuario usuarioLogado = securityService.getUsuarioLogado();


        if (!avaliacao.getReserva().getCliente().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem permissão para editar esta avaliação.");
        }


        avaliacao.setNota(dados.nota());
        avaliacao.setComentario(dados.comentario());

        return AvaliacaoResponseDTO.from(avaliacaoRepository.save(avaliacao));
    }


    public void deletar(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Usuario usuarioLogado = securityService.getUsuarioLogado();


        boolean isAutor = avaliacao.getReserva().getCliente().getId().equals(usuarioLogado.getId());
        boolean isAdmin = usuarioLogado.getTipoUsuario() == TipoUsuario.ADMIN;

        if (!isAutor && !isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para deletar esta avaliação.");
        }

        avaliacaoRepository.delete(avaliacao);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorQuarto(Long quartoId) {
        return avaliacaoRepository.findAllByQuartoId(quartoId).stream()
                .map(AvaliacaoResponseDTO::from)
                .toList();
    }
}