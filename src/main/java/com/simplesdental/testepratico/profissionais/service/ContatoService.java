package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.Contato;
import com.simplesdental.testepratico.profissionais.model.ContatoRequestDto;
import com.simplesdental.testepratico.profissionais.model.ContatoResponseDto;
import com.simplesdental.testepratico.profissionais.repository.ContatoRepository;
import com.simplesdental.testepratico.profissionais.utils.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContatoService implements SearchAndFilterInterface<ContatoResponseDto> {

    private final ContatoRepository contatoRepository;
    private final FilterUtils<ContatoResponseDto> filterUtils;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository, FilterUtils filterUtils) {
        this.contatoRepository = contatoRepository;
        this.filterUtils = filterUtils;
    }

    public ContatoResponseDto findById(Long id) {
        var contato = contatoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Contato", id));

        return contato.toContatoResponseDto();
    }

    public ContatoResponseDto insert(ContatoRequestDto contatoRequestDto) {
        var contato = contatoRequestDto.toContato();
        var contatoSalvo = contatoRepository.save(contato);

        return contatoSalvo.toContatoResponseDto();
    }

    public void update(Long id, ContatoRequestDto contatoRequestDto) {
        var contato = contatoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Contato", id));
        contato.setNome(contatoRequestDto.getNome());

        contatoRepository.save(contato);
    }

    public void delete(Long id) {
        contatoRepository.deleteById(id);
    }

    @Override
    public List<ContatoResponseDto> searchAndFilter(String query, List<String> fields) {
        var contatos = contatoRepository.findAll();
        var contatosResponseDtos = convertContatosToContatosResponseDto(contatos);

        contatosResponseDtos = filterUtils.filterObjectsByText(contatosResponseDtos, query, this::extractObjectAttributes);

        if (!fields.isEmpty()) {
            contatosResponseDtos = filterUtils.filterFields(contatosResponseDtos, fields);
        }

        return contatosResponseDtos;
    }

    @Override
    public String[] extractObjectAttributes(ContatoResponseDto contato) {
        return new String[] {
                contato.getNome(), contato.getProfissional().getNome(), contato.getCreatedDate().toString(), contato.getId().toString()
        };
    }

    private List<ContatoResponseDto> convertContatosToContatosResponseDto(List<Contato> contatos) {
        List<ContatoResponseDto> contatosResponseDtos = new ArrayList<>();

        for (var contato : contatos) {
            contatosResponseDtos.add(contato.toContatoResponseDto());
        }

        return contatosResponseDtos;
    }
}
