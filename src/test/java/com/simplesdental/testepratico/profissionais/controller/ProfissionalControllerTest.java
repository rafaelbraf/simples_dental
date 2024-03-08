package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.model.Cargo;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.service.ProfissionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfissionalControllerTest {

    @InjectMocks
    ProfissionalController profissionalController;

    @Mock
    ProfissionalService profissionalService;

    @Test
    void testGetAll() {
        var profissionaisMock = Arrays.asList(
                buildProfissional(1L, "Profissional Teste 1", Cargo.DESENVOLVEDOR, new Date(), new Date(), true),
                buildProfissional(2L, "Profissional Teste 2", Cargo.DESIGNER, new Date(), new Date(), true)
        );

        when(profissionalService.getAll()).thenReturn(profissionaisMock);

        var response = profissionalController.getAll("teste", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissionaisMock.size(), response.getBody().size());
    }

    @Test
    void testGetById_ProfissionalEncontrado() {
        var idProfissional = 1L;
        var profissionalMock = buildProfissional(idProfissional, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);

        when(profissionalService.getById(idProfissional)).thenReturn(Optional.of(profissionalMock));

        var response = profissionalController.getById(idProfissional);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissionalMock.getId(), response.getBody().getId());
    }

    @Test
    void testGetById_ProfissionalNaoEncontrado() {
        var response = profissionalController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testInsert() {
        var profissionalMock = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        when(profissionalService.insert(any(Profissional.class))).thenReturn(profissionalMock);

        var response = profissionalController.insert(profissionalMock);
        var mensagemEsperada = String.format("Sucesso profissional com id %s cadastrado", profissionalMock.getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody().toString());
    }

    @Test
    void testUpdate() {
        var idProfissional = 1L;
        var profissional = buildProfissional(idProfissional, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        when(profissionalService.existsById(idProfissional)).thenReturn(true);

        var response = profissionalController.update(idProfissional, profissional);
        var mensagemEsperada = Map.of("mensagem", "Profissional atualizado com sucesso!");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    @Test
    void testDelete() {
        var response = profissionalController.delete(1L);
        var mensagemEsperada = Map.of("mensagem", "Sucesso profissional excluído");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    private Profissional buildProfissional(Long id, String nome, Cargo cargo, Date nascimento, Date createdDate, boolean ativo) {
        return Profissional.builder()
                .id(id)
                .nome(nome)
                .cargo(cargo)
                .nascimento(nascimento)
                .createdDate(createdDate)
                .ativo(ativo)
                .build();
    }
}
