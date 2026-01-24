package com.GardenJava.app.service;

import com.GardenJava.app.dto.login.LoginRequestDTO;
import com.GardenJava.app.dto.login.RegistroRequestDTO;
import com.GardenJava.app.dto.login.ResponseDTO;
import com.GardenJava.app.infra.security.TokenService;
import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;
import com.GardenJava.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO login(LoginRequestDTO body) {
        Usuario usuario = repository.findByEmail(body.email())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(body.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Email ou senha inválidos");
        }

        String token = tokenService.generateToken(usuario);
        return new ResponseDTO(usuario.getNomeCompleto(), token);
    }

    @Transactional
    public ResponseDTO registrar(RegistroRequestDTO body) {
        if (repository.existsByEmail(body.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(body.nomeCompleto());
        novoUsuario.setEmail(body.email());
        novoUsuario.setSenha(passwordEncoder.encode(body.senha()));
        novoUsuario.setTipoDocumento(body.documento());
        novoUsuario.setNumeroDeDocumento(body.numeroDeDocumento());
        novoUsuario.setDataDeNascimento(body.dataDeNascimento());
        novoUsuario.setNumeroDeTelefone(body.numeroDeTelefone());

        // Registro público é sempre CLIENTE
        novoUsuario.setTipoUsuario(TipoUsuario.CLIENTE);

        repository.save(novoUsuario);

        String token = tokenService.generateToken(novoUsuario);
        return new ResponseDTO(novoUsuario.getNomeCompleto(), token);
    }
}