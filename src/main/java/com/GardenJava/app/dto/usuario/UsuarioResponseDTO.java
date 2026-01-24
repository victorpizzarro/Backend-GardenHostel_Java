package com.GardenJava.app.dto.usuario;

import com.GardenJava.app.model.usuario.TipoDocumento;
import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;

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

) {
    public static UsuarioResponseDTO from(Usuario entity) {
        if (entity == null) return null;

        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNomeCompleto(),
                entity.getEmail(),
                entity.getTipoDocumento(),
                entity.getNumeroDeDocumento(),
                entity.getDataDeNascimento(),
                entity.getNumeroDeTelefone(),
                entity.getTipoUsuario(),
                entity.getDataCriacao()
        );
    }
}
