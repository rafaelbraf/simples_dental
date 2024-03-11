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
import java.util.Optional;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final FilterUtils<Contato> filterUtils;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository, FilterUtils filterUtils) {
        this.contatoRepository = contatoRepository;
        this.filterUtils = filterUtils;
    }

    public List<ContatoResponseDto> searchAndFilterContatos(String query, List<String> fields) {
        var contatos = contatoRepository.findAll();
        contatos = filterUtils.filterObjectsByText(contatos, query, this::extractContatoAttributes);

        if (!fields.isEmpty()) {
            contatos = filterUtils.filterFields(contatos, fields);
        }

        var contatosResponseDtos = new ArrayList<ContatoResponseDto>();

        for (Contato contato : contatos) {
            contatosResponseDtos.add(contato.toContatoResponseDto());
        }

        return contatosResponseDtos;
    }

    public ContatoResponseDto getById(Long id) {
        var contato = contatoRepository.findById(id);
        if (!contato.isPresent()) {
            throw ResourceNotFoundException.forResource("Contato", id);
        }

        var contatoResponseDto = contato.get().toContatoResponseDto();

        return contatoResponseDto;
    }

    public ContatoResponseDto insert(ContatoRequestDto contatoRequestDto) {
        var contato = contatoRequestDto.toContato();
        var contatoSalvo = contatoRepository.save(contato);

        return contatoSalvo.toContatoResponseDto();
    }

    public void update(Long id, ContatoRequestDto contatoRequestDto) {
        var contatoCadastrado = contatoRepository.findById(id);
        if (!contatoCadastrado.isPresent()) {
            throw ResourceNotFoundException.forResource("Contato", id);
        }

        var contato = contatoCadastrado.get();
        contato.setNome(contatoRequestDto.getNome());

        contatoRepository.save(contato);
    }

    public void delete(Long id) {
        contatoRepository.deleteById(id);
    }

    private String[] extractContatoAttributes(Contato contato) {
        return new String[] {
                contato.getNome()
        };
    }
}
