package com.example.sistemadescarte.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sistemadescarte.entities.NotaDescarteEntity;

public interface NotaDescarteRepository extends JpaRepository<NotaDescarteEntity, Integer> {
}
