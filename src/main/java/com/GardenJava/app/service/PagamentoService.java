package com.GardenJava.app.service;

import com.GardenJava.app.dto.pagamento.PagamentoRequestDTO;
import com.GardenJava.app.dto.pagamento.PagamentoResponseDTO;
import com.GardenJava.app.model.pagamento.OrigemPagamento;
import com.GardenJava.app.model.pagamento.Pagamento;
import com.GardenJava.app.model.pagamento.StatusPagamento;
import com.GardenJava.app.model.reserva.Reserva;
import com.GardenJava.app.model.reserva.StatusReserva;
import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;
import com.GardenJava.app.repository.PagamentoRepository;
import com.GardenJava.app.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    public PagamentoResponseDTO confirmarPagamento(PagamentoRequestDTO dados) {


        Reserva reserva = reservaRepository.findById(dados.reservaId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));


        Usuario logado = usuarioAutenticado();


        if (dados.origem() == OrigemPagamento.ONLINE) {

            if (!reserva.getCliente().getId().equals(logado.getId())) {
                throw new AccessDeniedException("Pagamento online só pode ser realizado pelo titular da reserva.");
            }
        } else if (dados.origem() == OrigemPagamento.BALCAO) {

            if (logado.getTipoUsuario() == TipoUsuario.CLIENTE) {
                throw new AccessDeniedException("Clientes não têm permissão para registrar pagamentos de balcão.");
            }
        }


        if (reserva.getStatusReserva() != StatusReserva.PENDENTE) {
            throw new RuntimeException("Esta reserva não está aguardando pagamento. Status atual: " + reserva.getStatusReserva());
        }

        Pagamento pagamento = new Pagamento(
                reserva,
                reserva.getValorTotalDiarias(),
                dados.formaPagamento(),
                dados.origem()
        );


        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamentoRepository.save(pagamento);

        reserva.setStatusReserva(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva);


        return PagamentoResponseDTO.from(pagamento);
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO detalhar(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        Usuario logado = usuarioAutenticado();


        if (logado.getTipoUsuario() == TipoUsuario.CLIENTE &&
                !pagamento.getReserva().getCliente().getId().equals(logado.getId())) {
            throw new AccessDeniedException("Acesso negado: Você não pode visualizar pagamentos de terceiros.");
        }

        return PagamentoResponseDTO.from(pagamento);
    }

    private Usuario usuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Usuário não autenticado no contexto de segurança.");
        }
        return (Usuario) auth.getPrincipal();
    }
}