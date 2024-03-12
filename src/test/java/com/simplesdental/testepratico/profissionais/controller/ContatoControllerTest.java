package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.model.Cargo;
import com.simplesdental.testepratico.profissionais.model.ContatoRequestDto;
import com.simplesdental.testepratico.profissionais.model.ContatoResponseDto;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.service.ContatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContatoControllerTest {

    @InjectMocks
    private ContatoController contatoController;

    @Mock
    private ContatoService contatoService;

    private ContatoRequestDto contatoRequestDto;
    private ContatoResponseDto contatoResponseDto;
    private Profissional profissional;

    @BeforeEach
    void setup() {
        profissional = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        contatoRequestDto = buildContatoRequestDto("Contato 1", profissional);
        contatoResponseDto = buildContatoResponseDto(1L, "Contato 1", new Date(), profissional);
    }

    @Test
    void testGetAllContatos() {
        var contatosReponseDtos = Arrays.asList(
                contatoResponseDto,
                buildContatoResponseDto(2L, "Contato 2", new Date(), profissional)
        );

        when(contatoService.searchAndFilterContatos(anyString(), anyList())).thenReturn(contatosReponseDtos);

        var response = contatoController.getAll("", Collections.emptyList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatosReponseDtos.size(), response.getBody().size());
        assertEquals(profissional.getId(), response.getBody().get(0).getProfissional().getId());
    }

    @Test
    void testGetById_ContatoEncontrado() {
        when(contatoService.getById(anyLong())).thenReturn(contatoResponseDto);

        var response = contatoController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatoResponseDto.getId(), response.getBody().getId());
    }

    @Test
    void testInsertContato() {
        when(contatoService.insert(any(ContatoRequestDto.class))).thenReturn(contatoResponseDto);

        var response = contatoController.insert(contatoRequestDto);
        var mensagemEsperada = String.format("Contato cadastrado com sucesso para Profissional %s.", contatoResponseDto.getProfissional().getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody().get("mensagem"));
    }

    @Test
    void testUpdateContatoById() {
        var response = contatoController.update(1L, contatoRequestDto);
        var mensagemEsperada = Map.of("mensagem", "Contato atualizado com sucesso!");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    @Test
    void testDeleteContatoById() {
        var response = contatoController.delete(1L);
        var mensagemEsperada = Map.of("mensagem", "Contato excluído!");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    private ContatoRequestDto buildContatoRequestDto(String nome, Profissional profissional) {
        return ContatoRequestDto.builder()
                .nome(nome)
                .profissional(profissional)
                .build();
    }

    private ContatoResponseDto buildContatoResponseDto(Long id, String nome, Date createdDate, Profissional profissional) {
        return ContatoResponseDto.builder()
                .id(id)
                .nome(nome)
                .createdDate(createdDate)
                .profissional(profissional)
                .build();
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
