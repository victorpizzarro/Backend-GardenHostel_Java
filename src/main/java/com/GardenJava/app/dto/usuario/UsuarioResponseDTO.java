package com.GardenJava.app.dto.usuario;

import com.GardenJava.app.model.usuario.TipoDocumento;
import com.GardenJava.app.model.usuario.TipoUsuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nomeCompleto,
        String email,
        TipoDocumento documento,
        String numeroDeDocumento,
        LocalDate dataDeNascimento,
        String numeroDeTelefone,
        TipoUsuario usuario,
        LocalDateTime dataCriacao

) {}
