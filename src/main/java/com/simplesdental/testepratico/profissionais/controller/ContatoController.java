package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.model.Contato;
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
    public ResponseEntity<List<Contato>> getAll() {
        var contatos = contatoService.getAll();
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> getById(@PathVariable Long id) {
        return contatoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> insert(@RequestBody Contato contato) {
        var contatoCadastrado = contatoService.insert(contato);
        var mensagem = String.format("Contato cadastrado com sucesso para usuário %s.", contatoCadastrado.getProfissional().getId());
        var retornoMap = Map.of("mensagem", mensagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(retornoMap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody Contato contato) {
        if (!contatoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        contatoService.update(id, contato);
        return ResponseEntity.ok(Map.of("mensagem", "Contato atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        contatoService.delete(id);
        return ResponseEntity.ok(Map.of("mensagem", "Contato excluído!"));
    }

}
