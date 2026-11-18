package com.example.sistemadescarte.repositories;

import com.example.sistemadescarte.entities.EmpresaColetoraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaColetoraRepository extends JpaRepository<EmpresaColetoraEntity, Integer> {
}