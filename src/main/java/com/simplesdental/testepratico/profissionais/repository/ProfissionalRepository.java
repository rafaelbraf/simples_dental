package com.simplesdental.testepratico.profissionais.repository;

import com.simplesdental.testepratico.profissionais.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}
