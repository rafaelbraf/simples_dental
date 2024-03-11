package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.Contato;
import com.simplesdental.testepratico.profissionais.model.ContatoResponseDto;
import com.simplesdental.testepratico.profissionais.model.ContatoRequestDto;
import com.simplesdental.testepratico.profissionais.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private ContatoService contatoService;

    @Autowired
    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping
    public ResponseEntity<List<ContatoResponseDto>> getAll(@RequestParam String q, @RequestParam(required = false) List<String> fields) {
        var contatos = contatoService.searchAndFilterContatos(q, fields);
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> getById(@PathVariable Long id) {
        var contato = contatoService.getById(id);
        return ResponseEntity.ok(contato);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> insert(@RequestBody ContatoRequestDto contatoRequestDto) {
        try {
            var contatoCadastrado = contatoService.insert(contatoRequestDto);
            var mensagem = String.format("Contato cadastrado com sucesso para Profissional %s.", contatoCadastrado.getProfissional().getId());
            var retornoMap = Map.of("mensagem", mensagem);

            return ResponseEntity.status(HttpStatus.CREATED).body(retornoMap);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody ContatoRequestDto contatoRequestDto) {
        try {
            contatoService.update(id, contatoRequestDto);
            return ResponseEntity.ok(Map.of("mensagem", "Contato atualizado com sucesso!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            contatoService.delete(id);
            return ResponseEntity.ok(Map.of("mensagem", "Contato excluído!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
