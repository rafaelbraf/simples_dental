package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.model.ProfissionalRequestDto;
import com.simplesdental.testepratico.profissionais.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<List<Profissional>> getAll(@RequestParam String q, @RequestParam(required = false) List<String> fields) {
        var profissionais = profissionalService.searchAndFilterProfissionais(q, fields);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissional> getById(@PathVariable Long id) {
        var profissional = profissionalService.getById(id);
        return ResponseEntity.ok(profissional);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> insert(@Valid @RequestBody ProfissionalRequestDto profissionalRequestDto) {
        try {
            var profissionalCadastrado = profissionalService.insert(profissionalRequestDto);
            var mensagem = String.format("Profissional cadastrado com sucesso com id %s", profissionalCadastrado.getId());
            var responseMap = Map.of("mensagem", mensagem);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @Valid @RequestBody ProfissionalRequestDto profissionalRequestDto) {
        try {
            profissionalService.update(id, profissionalRequestDto);
            return ResponseEntity.ok(Map.of("mensagem", "Profissional atualizado com sucesso!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            profissionalService.delete(id);
            return ResponseEntity.ok(Map.of("mensagem", "Sucesso profissional excluído"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
