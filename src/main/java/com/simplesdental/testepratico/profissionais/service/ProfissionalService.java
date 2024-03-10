package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.repository.ProfissionalRepository;
import com.simplesdental.testepratico.profissionais.utils.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final FilterUtils<Profissional> filterUtils;

    @Autowired
    public ProfissionalService(ProfissionalRepository profissionalRepository, FilterUtils<Profissional> filterUtils) {
        this.profissionalRepository = profissionalRepository;
        this.filterUtils = filterUtils;
    }

    public List<Profissional> searchAndFilterProfissionais(String query, List<String> fields) {
        var profissionais = profissionalRepository.findAll();
        profissionais = filterUtils.filterObjectsByText(profissionais, query, this::extractProfissionalAttributes);

        if (!fields.isEmpty()) {
            profissionais = filterUtils.filterFields(profissionais, fields);
        }

        return profissionais;
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

    private String[] extractProfissionalAttributes(Profissional profissional) {
        return new String[] {
                profissional.getNome(), profissional.getCargo().toString(),
        };
    }
}
