package com.GardenJava.app.dto.usuario;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;


public record UsuarioRequestDTO(

        @NotBlank
        String nomeCompleto,

        String numeroDeTelefone,

        LocalDate dataDeNascimento

) {}
