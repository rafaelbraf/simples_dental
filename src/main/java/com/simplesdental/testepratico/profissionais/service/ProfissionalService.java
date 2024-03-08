package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    @Autowired
    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public List<Profissional> getAll() {
        return profissionalRepository.findAll();
    }

    public Optional<Profissional> getById(Long id) {
        return profissionalRepository.findById(id);
    }

    public Profissional insert(Profissional profissional) {
        return profissionalRepository.save(profissional);
    }

    public void update(Long id, Profissional profissionalRequest) {
        var profissionalCadastrado = profissionalRepository.findById(id);

        if (profissionalCadastrado.isPresent()) {
            var profissional = profissionalCadastrado.get();
            profissional.setNome(profissionalRequest.getNome());
            profissional.setCargo(profissionalRequest.getCargo());
            profissional.setNascimento(profissionalRequest.getNascimento());
            profissional.setCreatedDate(profissionalRequest.getCreatedDate());
            profissional.setAtivo(profissionalRequest.isAtivo());

            profissionalRepository.save(profissional);
        }
    }

    public void delete(Long id) {
        var profissionalCadastrado = profissionalRepository.findById(id);

        if (profissionalCadastrado.isPresent() && profissionalCadastrado.get().isAtivo()) {
            var profissional = profissionalCadastrado.get();
            profissional.setAtivo(false);

            profissionalRepository.save(profissional);
        }
    }

    public boolean existsById(Long id) {
        return profissionalRepository.existsById(id);
    }
}
