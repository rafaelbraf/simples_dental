package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.model.Cargo;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.model.ProfissionalRequestDto;
import com.simplesdental.testepratico.profissionais.service.ProfissionalService;
import org.junit.jupiter.api.BeforeEach;
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
    private ProfissionalController profissionalController;

    @Mock
    private ProfissionalService profissionalService;

    private Profissional profissional;
    private ProfissionalRequestDto profissionalRequestDto;
    private Date nascimento;

    @BeforeEach
    void setup() {
        nascimento = new Date();
        profissional = buildProfissional(1L, "Profissional Teste 1", Cargo.DESENVOLVEDOR, nascimento, new Date(), true);
        profissionalRequestDto = buildProfissionalRequestDTO("Profissional Teste", Cargo.DESENVOLVEDOR, new Date());
    }

    @Test
    void testGetAllProfissionais() {
        var profissionaisMock = Arrays.asList(
                profissional,
                buildProfissional(2L, "Profissional Teste 2", Cargo.DESIGNER, new Date(), new Date(), true)
        );

        when(profissionalService.searchAndFilter(anyString(), anyList())).thenReturn(profissionaisMock);

        var response = profissionalController.getAll("", Collections.emptyList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissionaisMock.size(), response.getBody().size());
    }

    @Test
    void testGetProfissionaisComFiltro() {
        when(profissionalService.searchAndFilter(anyString(), anyList())).thenReturn(List.of(profissional));

        var response = profissionalController.getAll("desenvolvedor", Collections.emptyList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissional.getId(), response.getBody().get(0).getId());
    }

    @Test
    void testGetProfissionaisComFieldsEscolhidos() {
        var profissionalMock = buildProfissional(null, "Profissional Teste 1", null, nascimento, null, false);
        when(profissionalService.searchAndFilter(anyString(), anyList())).thenReturn(List.of(profissionalMock));

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
        when(profissionalService.findById(anyLong())).thenReturn(profissional);

        var response = profissionalController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profissional.getId(), response.getBody().getId());
    }

    @Test
    void testInsertProfissional() {
        when(profissionalService.insert(any(ProfissionalRequestDto.class))).thenReturn(profissional);

        var response = profissionalController.insert(profissionalRequestDto);
        var mensagemKey = "mensagem";
        var mensagemEsperadaMap = Map.of(mensagemKey, String.format("Profissional cadastrado com sucesso com id %s", profissional.getId()));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mensagemEsperadaMap.get(mensagemKey), response.getBody().get(mensagemKey));
    }

    @Test
    void testUpdateProfissionalById() {
        var response = profissionalController.update(1L, profissionalRequestDto);
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

    private ProfissionalRequestDto buildProfissionalRequestDTO(String nome, Cargo cargo, Date nascimento) {
        return ProfissionalRequestDto.builder()
                .nome(nome)
                .cargo(cargo)
                .nascimento(nascimento)
                .build();
    }
}
