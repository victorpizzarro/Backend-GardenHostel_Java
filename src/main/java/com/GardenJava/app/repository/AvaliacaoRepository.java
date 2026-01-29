package com.GardenJava.app.repository;

import com.GardenJava.app.model.avaliacao.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    boolean existsByReservaId(Long reservaId);


    @Query("SELECT a FROM Avaliacao a WHERE a.reserva.vaga.quarto.id = :quartoId")
    List<Avaliacao> findAllByQuartoId(Long quartoId);
}