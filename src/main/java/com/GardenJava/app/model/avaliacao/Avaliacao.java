package com.GardenJava.app.model.avaliacao;


import com.GardenJava.app.model.reserva.Reserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nota de 1 a 5
    @Column(nullable = false)
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    private LocalDateTime dataAvaliacao = LocalDateTime.now();


    @OneToOne
    @JoinColumn(name = "reserva_id", unique = true, nullable = false)
    private Reserva reserva;
}
