package com.GardenJava.app.model.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipoDocumento;

    private String numeroDeDocumento;

    private LocalDate dataDeNascimento;

    private String numeroDeTelefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @ManyToOne
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public void atualizarPerfil(String nomeCompleto, String numeroDeTelefone, LocalDate dataDeNascimento) {
        this.nomeCompleto = nomeCompleto;
        this.numeroDeTelefone = numeroDeTelefone;
        this.dataDeNascimento = dataDeNascimento;
    }

    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
    }

    public void alterarEmail(String novoEmail) {
        this.email = novoEmail;
    }
}
