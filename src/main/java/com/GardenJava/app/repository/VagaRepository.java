package com.GardenJava.app.repository;


import com.GardenJava.app.model.vaga.StatusVaga;
import com.GardenJava.app.model.vaga.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByQuartoId(Long quartoId);

    List<Vaga> findAllByStatus(StatusVaga status);

    long countByQuartoId(Long quartoId);
}