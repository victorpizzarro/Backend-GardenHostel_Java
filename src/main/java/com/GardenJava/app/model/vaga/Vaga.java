package com.GardenJava.app.model.vaga;

import com.GardenJava.app.model.quarto.Quarto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vagas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeIdentificador;

    private String descricaoPeculiaridadesPt;
    private String descricaoPeculiaridadesEn;

    @Enumerated(EnumType.STRING)
    private StatusVaga status = StatusVaga.LIVRE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;
}