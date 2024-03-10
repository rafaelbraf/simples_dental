package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    @Autowired
    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public List<Profissional> searchAndFilterProfissionais(String query, List<String> fields) {
        var profissionais = profissionalRepository.findAll();
        profissionais = filterProfissionaisByQuery(profissionais, query);

        if (!fields.isEmpty()) {
            profissionais = filterFields(profissionais, fields);
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

    private List<Profissional> filterFields(List<Profissional> profissionais, List<String> fields) {
        return profissionais.stream()
                .map(profissional -> profissionalWithFieldsFiltered(profissional, fields))
                .collect(Collectors.toList());
    }

    private Profissional profissionalWithFieldsFiltered(Profissional profissional, List<String> fields) {
        var profissionalComCamposFiltrados = Profissional.builder().build();
        Class<?> profissionalClass = Profissional.class;

        fields.forEach(field -> {
            try {
                var fieldClass = profissionalClass.getDeclaredField(field);
                fieldClass.setAccessible(true);

                Object value = fieldClass.get(profissional);
                fieldClass.set(profissionalComCamposFiltrados, value);
            } catch (NoSuchFieldException | IllegalAccessException e){
                throw new RuntimeException(e);
            }
        });

        return profissionalComCamposFiltrados;
    }

    private List<Profissional> filterProfissionaisByQuery(List<Profissional> profissionais, String query) {
        if (isNull(query) || query.isEmpty()) {
            return profissionais;
        }

        String queryLowerCase = query.toLowerCase();

        return profissionais.stream()
                .filter(profissional -> containsValueInAttributes(profissional, queryLowerCase))
                .collect(Collectors.toList());
    }

    private boolean containsValueInAttributes(Profissional profissional, String value) {
        return profissional.getNome().toLowerCase().contains(value) ||
                profissional.getCargo().toString().toLowerCase().contains(value);
    }
}
