package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.model.ProfissionalRequestDto;
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

    public Profissional getById(Long id) {
        return findProfissionalById(id);
    }

    public Profissional insert(ProfissionalRequestDto profissionalRequestDto) {
        var profissional = profissionalRequestDto.toProfissional();
        return profissionalRepository.save(profissional);
    }

    public void update(Long id, ProfissionalRequestDto profissionalRequest) {
        var profissional = findProfissionalById(id);
        profissional.setNome(profissionalRequest.getNome());
        profissional.setCargo(profissionalRequest.getCargo());
        profissional.setNascimento(profissionalRequest.getNascimento());
        profissional.setCreatedDate(profissionalRequest.getCreatedDate());
        profissional.setAtivo(profissionalRequest.isAtivo());

        profissionalRepository.save(profissional);
    }

    public void delete(Long id) {
        var profissionalCadastrado = findProfissionalById(id);
        if (profissionalCadastrado.isAtivo()) {
            var profissional = profissionalCadastrado;
            profissional.setAtivo(false);

            profissionalRepository.save(profissional);
        }
    }

    public boolean existsById(Long id) {
        return profissionalRepository.existsById(id);
    }

    private String[] extractProfissionalAttributes(Profissional profissional) {
        return new String[] {
                profissional.getId().toString(), profissional.getNome(), profissional.getCargo().toString(), profissional.getNascimento().toString(), profissional.getCreatedDate().toString(),
                profissional.getContatos().toString(), String.valueOf(profissional.isAtivo())
        };
    }

    private Profissional findProfissionalById(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Profissional", id));
    }
}
