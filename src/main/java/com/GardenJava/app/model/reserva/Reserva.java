package com.GardenJava.app.model.reserva;

import com.GardenJava.app.model.usuario.Usuario;
import com.GardenJava.app.model.vaga.Vaga;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp; // Dica Pro

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "data_checkin")
    private LocalDateTime dataCheckin;

    @Column(name = "data_checkout")
    private LocalDateTime dataCheckout;


    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "valor_total_diarias")
    private BigDecimal valorTotalDiarias;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_reserva")
    private OrigemReserva origemReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_reserva")
    private StatusReserva statusReserva;


    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "atendente_id", nullable = true)
    private Usuario atendente;


    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;
}