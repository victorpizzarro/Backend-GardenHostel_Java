package com.GardenJava.app.model.quarto;

import com.GardenJava.app.model.vaga.Vaga;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quartos")
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricaoPt;
    private String descricaoEn;

    @Enumerated(EnumType.STRING)
    private TipoQuarto tipoQuarto;

    private int capacidade;
    private boolean banheiroPrivativo;
    private BigDecimal precoDiaria;



    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vaga> vagas = new ArrayList<>();

    public void atualizarDados(
            String nome,
            String descricaoPt,
            String descricaoEn,
            TipoQuarto tipoQuarto,
            int capacidade,
            boolean banheiroPrivativo,
            BigDecimal precoDiaria
    ) {
        this.nome = nome;
        this.descricaoPt = descricaoPt;
        this.descricaoEn = descricaoEn;
        this.tipoQuarto = tipoQuarto;
        this.capacidade = capacidade;
        this.banheiroPrivativo = banheiroPrivativo;
        this.precoDiaria = precoDiaria;
    }


    public void adicionarVaga(Vaga vaga) {
        vagas.add(vaga);
        vaga.setQuarto(this);
    }
}