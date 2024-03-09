package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.model.Contato;
import com.simplesdental.testepratico.profissionais.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {

    private ContatoRepository contatoRepository;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public List<Contato> getAll() {
        return contatoRepository.findAll();
    }

    public Optional<Contato> getById(Long id) {
        return contatoRepository.findById(id);
    }

    public Contato insert(Contato contato) {
        return contatoRepository.save(contato);
    }

    public void update(Long id, Contato contatoRequest) {
        var contatoCadastrado = contatoRepository.findById(id);

        if (contatoCadastrado.isPresent()) {
            var contato = contatoCadastrado.get();
            contato.setNome(contatoRequest.getNome());

            contatoRepository.save(contato);
        }
    }

    public void delete(Long id) {
        contatoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return contatoRepository.existsById(id);
    }
}
