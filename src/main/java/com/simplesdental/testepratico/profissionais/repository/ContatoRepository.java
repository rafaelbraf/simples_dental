package com.simplesdental.testepratico.profissionais.repository;

import com.simplesdental.testepratico.profissionais.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
}
