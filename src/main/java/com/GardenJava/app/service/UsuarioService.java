package com.GardenJava.app.service;

import com.GardenJava.app.dto.usuario.UsuarioRequestDTO;
import com.GardenJava.app.dto.usuario.UsuarioResponseDTO;
import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;
import com.GardenJava.app.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;



    public UsuarioResponseDTO buscar(UUID id) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.LEITURA);
        return toResponseDTO(alvo);
    }

    public List<UsuarioResponseDTO> listar() {
        Usuario logado = usuarioAutenticado();

        if (logado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new AccessDeniedException("Apenas ADMIN pode listar usuários");
        }

        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public UsuarioResponseDTO editar(UUID id, UsuarioRequestDTO body) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.EDITAR_PERFIL);


        alvo.atualizarPerfil(
                body.nomeCompleto(),
                body.numeroDeTelefone(),
                body.dataDeNascimento()
        );

        return toResponseDTO(repository.save(alvo));
    }

    public UsuarioResponseDTO alterarEmail(UUID id, String novoEmail) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.ALTERAR_EMAIL);

        if (repository.existsByEmail(novoEmail)) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        alvo.alterarEmail(novoEmail);
        return toResponseDTO(repository.save(alvo));
    }

    public void alterarSenha(UUID id, String novaSenha) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.ALTERAR_SENHA);

        alvo.alterarSenha(passwordEncoder.encode(novaSenha));
        repository.save(alvo);
    }



    public void deletar(UUID id) {
        Usuario alvo = buscarUsuario(id);
        Usuario logado = usuarioAutenticado();

        if (logado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new AccessDeniedException("Apenas ADMIN pode deletar usuários");
        }

        repository.delete(alvo);
    }



    private void validarAcesso(Usuario alvo, TipoAcao acao) {
        Usuario logado = usuarioAutenticado();

        if (logado.getTipoUsuario() == TipoUsuario.ADMIN) {
            return;
        }

        if (logado.getTipoUsuario() == TipoUsuario.ATENDENTE) {

            if (acao == TipoAcao.LEITURA || acao == TipoAcao.EDITAR_PERFIL) {
                return;
            }
            throw new AccessDeniedException("Atendente não pode executar essa ação");
        }


        if (!logado.getId().equals(alvo.getId())) {
            throw new AccessDeniedException("Você não pode acessar outro usuário");
        }


        if (acao == TipoAcao.LEITURA
                || acao == TipoAcao.EDITAR_PERFIL
                || acao == TipoAcao.ALTERAR_EMAIL
                || acao == TipoAcao.ALTERAR_SENHA) {
            return;
        }

        throw new AccessDeniedException("Ação não permitida");
    }

    private Usuario usuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (Usuario) auth.getPrincipal();
    }

    private Usuario buscarUsuario(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }



    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getTipoDocumento(),
                usuario.getNumeroDeDocumento(),
                usuario.getDataDeNascimento(), // Agora é LocalDate
                usuario.getNumeroDeTelefone(),
                usuario.getTipoUsuario(),
                usuario.getDataCriacao()
        );
    }


    private enum TipoAcao {
        LEITURA,
        EDITAR_PERFIL,
        ALTERAR_EMAIL,
        ALTERAR_SENHA
    }
}