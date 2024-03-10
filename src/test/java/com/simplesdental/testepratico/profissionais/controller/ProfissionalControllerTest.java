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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfissionalControllerTest {

    @InjectMocks
    ProfissionalController profissionalController;

    @Mock
    ProfissionalService profissionalService;

    @Test
    void testGetAllProfissionais() {
        var profissionaisMock = Arrays.asList(
                buildProfissional(1L, "Profissional Teste 1", Cargo.DESENVOLVEDOR, new Date(), new Date(), true),
                buildProfissional(2L, "Profissional Teste 2", Cargo.DESIGNER, new Date(), new Date(), true)
        );

        when(profissionalService.searchAndFilterProfissionais(anyString(), anyList())).thenReturn(profissionaisMock);

        var response = profissionalController.getAll("", Collections.emptyList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissionaisMock.size(), response.getBody().size());
    }

    @Test
    void testGetProfissionaisComFiltro() {
        var profissional1 = buildProfissional(1L, "Profissional Teste 1", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);

        when(profissionalService.searchAndFilterProfissionais(anyString(), anyList())).thenReturn(List.of(profissional1));

        var response = profissionalController.getAll("desenvolvedor", Collections.emptyList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissional1.getId(), response.getBody().get(0).getId());
    }

    @Test
    void testGetProfissionaisComFieldsEscolhidos() {
        var profissional = Profissional.builder()
                .nome("Profissional Teste")
                .nascimento(new Date())
                .build();

        when(profissionalService.searchAndFilterProfissionais(anyString(), anyList())).thenReturn(List.of(profissional));

        var response = profissionalController.getAll("", List.of("nome", "nascimento"));
        var profissionalResponse = response.getBody().get(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissional.getNome(), profissionalResponse.getNome());
        assertEquals(profissional.getNascimento(), profissionalResponse.getNascimento());
        assertNull(profissionalResponse.getId());
        assertNull(profissionalResponse.getCargo());
        assertNull(profissionalResponse.getCreatedDate());
        assertNull(profissionalResponse.getContatos());
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
    void testInsertProfissional() {
        var profissionalMock = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        when(profissionalService.insert(any(Profissional.class))).thenReturn(profissionalMock);

        var response = profissionalController.insert(profissionalMock);
        var mensagemEsperada = String.format("Sucesso profissional com id %s cadastrado", profissionalMock.getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody().toString());
    }

    @Test
    void testUpdateProfissionalById() {
        var idProfissional = 1L;
        var profissional = buildProfissional(idProfissional, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        when(profissionalService.existsById(idProfissional)).thenReturn(true);

        var response = profissionalController.update(idProfissional, profissional);
        var mensagemEsperada = Map.of("mensagem", "Profissional atualizado com sucesso!");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    @Test
    void testDeleteProfissionalById() {
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
