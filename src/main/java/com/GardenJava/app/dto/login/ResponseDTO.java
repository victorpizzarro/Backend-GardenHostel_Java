package com.GardenJava.app.dto.login;

import com.GardenJava.app.model.usuario.Usuario;

public record ResponseDTO (String nome, String token, String role) {


    public static ResponseDTO from(Usuario usuario, String token) {
        return new ResponseDTO(
                usuario.getNomeCompleto(),
                token,
                usuario.getTipoUsuario().toString()
        );
    }
}