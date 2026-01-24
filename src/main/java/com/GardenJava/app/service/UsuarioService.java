package com.GardenJava.app.service;

import com.GardenJava.app.dto.login.RegistroRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscar(UUID id) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.LEITURA);
        return UsuarioResponseDTO.from(alvo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        Usuario logado = usuarioAutenticado();

        if (logado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new AccessDeniedException("Apenas ADMIN pode listar todos usuários");
        }

        return repository.findAll()
                .stream()
                .map(UsuarioResponseDTO::from)
                .toList();
    }

    public UsuarioResponseDTO editar(UUID id, UsuarioRequestDTO body) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.EDITAR_PERFIL);

        alvo.atualizarPerfil(
                body.nomeCompleto(),
                body.numeroDeTelefone(),
                body.dataDeNascimento()
        );

        return UsuarioResponseDTO.from(repository.save(alvo));
    }


    public UsuarioResponseDTO alterarEmail(UUID id, String novoEmail) {
        Usuario alvo = buscarUsuario(id);
        validarAcesso(alvo, TipoAcao.ALTERAR_EMAIL);

        if (repository.existsByEmail(novoEmail)) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        alvo.alterarEmail(novoEmail);
        return UsuarioResponseDTO.from(repository.save(alvo));
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

        if (logado.getTipoUsuario() == TipoUsuario.ADMIN) return;

        if (logado.getTipoUsuario() == TipoUsuario.ATENDENTE) {
            if (acao == TipoAcao.LEITURA || acao == TipoAcao.EDITAR_PERFIL) return;
            throw new AccessDeniedException("Atendente não tem permissão para esta ação");
        }

        if (!logado.getId().equals(alvo.getId())) {
            throw new AccessDeniedException("Você não pode acessar dados de outro usuário");
        }
    }

    private Usuario usuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }

    private Usuario buscarUsuario(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private enum TipoAcao { LEITURA, EDITAR_PERFIL, ALTERAR_EMAIL, ALTERAR_SENHA }
}