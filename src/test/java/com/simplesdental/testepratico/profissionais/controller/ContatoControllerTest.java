package com.simplesdental.testepratico.profissionais.controller;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.Cargo;
import com.simplesdental.testepratico.profissionais.model.Contato;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.service.ContatoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContatoControllerTest {

    @InjectMocks
    ContatoController contatoController;

    @Mock
    ContatoService contatoService;

    @Test
    void testGetAll() {
        var profissional = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        var contatosMock = Arrays.asList(
                buildContato(1L, "Contato 1", new Date(), profissional),
                buildContato(2L, "Contato 2", new Date(), profissional)
        );

        when(contatoService.getAll()).thenReturn(contatosMock);

        var response = contatoController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatosMock.size(), response.getBody().size());
        assertEquals(profissional.getId(), response.getBody().get(0).getProfissional().getId());
    }

    @Test
    void testGetById_ContatoEncontrado() {
        var idContato = 1L;
        var profissional = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        var contatoMock = buildContato(1L, "Contato 1", new Date(), profissional);

        when(contatoService.getById(idContato)).thenReturn(Optional.of(contatoMock));

        var response = contatoController.getById(idContato);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contatoMock.getId(), response.getBody().getId());
    }

    @Test
    void testGetById_ContatoNaoEncontrado() {
        assertThrows(ResourceNotFoundException.class, () -> contatoController.getById(1L));
    }

    @Test
    void testInsert() {
        var profissional = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        var contatoMock = buildContato(1L, "Contato 1", new Date(), profissional);

        when(contatoService.insert(any(Contato.class))).thenReturn(contatoMock);

        var response = contatoController.insert(contatoMock);
        var mensagemEsperada = String.format("Contato cadastrado com sucesso para Profissional %s.", contatoMock.getProfissional().getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody().get("mensagem"));
    }

    @Test
    void testUpdate() {
        var idContato = 1L;
        var profissional = buildProfissional(1L, "Profissional Teste", Cargo.DESENVOLVEDOR, new Date(), new Date(), true);
        var contato = buildContato(idContato, "Contato 1", new Date(), profissional);

        when(contatoService.existsById(idContato)).thenReturn(true);

        var response = contatoController.update(idContato, contato);
        var mensagemEsperada = Map.of("mensagem", "Contato atualizado com sucesso!");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    @Test
    void testDelete() {
        var response = contatoController.delete(1L);
        var mensagemEsperada = Map.of("mensagem", "Contato excluído!");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    private Contato buildContato(Long id, String nome, Date createdDate, Profissional profissional) {
        return Contato.builder()
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
