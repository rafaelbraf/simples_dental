package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @Autowired
    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> getAll(@RequestParam String q, @RequestParam(required = false) List<List<String>> fields) {
        var profissionais = profissionalService.getAll();

        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissional> getById(@PathVariable Long id) {
        return profissionalService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> insert(@RequestBody Profissional profissional) {
        var profissionalCadastrado = profissionalService.insert(profissional);
        var mensagem = String.format("Sucesso profissional com id %s cadastrado", profissionalCadastrado.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody Profissional profissional) {
        if (!profissionalService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profissionalService.update(id, profissional);
        return ResponseEntity.ok(Map.of("mensagem", "Profissional atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.ok(Map.of("mensagem", "Sucesso profissional excluído"));
    }
}
