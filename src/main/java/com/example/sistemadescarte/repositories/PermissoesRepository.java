package com.example.sistemadescarte.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sistemadescarte.entities.PermissoesEntity;

public interface PermissoesRepository extends JpaRepository<PermissoesEntity, Integer> {
    Optional<PermissoesEntity> findByNomeIgnoreCase(String nome);
}