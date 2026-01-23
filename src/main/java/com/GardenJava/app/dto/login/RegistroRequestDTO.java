package com.GardenJava.app.dto.login;

import com.GardenJava.app.model.usuario.TipoDocumento;
import com.GardenJava.app.model.usuario.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegistroRequestDTO(
        @NotBlank String nomeCompleto,
        @Email String email,
        @NotBlank String senha,
        @NotNull TipoDocumento documento,
        String numeroDeDocumento,
        LocalDate dataDeNascimento,
        String numeroDeTelefone,
        TipoUsuario usuario
) {
}
