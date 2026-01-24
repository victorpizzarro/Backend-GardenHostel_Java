package com.GardenJava.app.dto.usuario;

import com.GardenJava.app.model.usuario.TipoUsuario;
import com.GardenJava.app.model.usuario.Usuario;

import java.util.UUID;

public record UsuarioResumoDTO(
       UUID id,
       String nomeCompleto,
       String email,
       TipoUsuario tipoUsuario
) {
    public static UsuarioResumoDTO from(Usuario usuario){
        if (usuario == null) return null;

        return new UsuarioResumoDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }
}
