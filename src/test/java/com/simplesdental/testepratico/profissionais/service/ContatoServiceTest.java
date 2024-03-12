package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.*;
import com.simplesdental.testepratico.profissionais.repository.ContatoRepository;
import com.simplesdental.testepratico.profissionais.utils.FilterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private FilterUtils<Contato> filterUtils;

    private Contato contato;
    private ContatoRequestDto contatoRequestDto;
    private Profissional profissional;

    @BeforeEach
    void setup() {
        profissional = buildProfissional();
        contato = buildContato(1L, "Contato 1", profissional, new Date());
        contatoRequestDto = buildContatoRequestDto(contato.getNome(), contato.getProfissional());
    }

    @Test
    public void testSearchAndFilterContatos() {
        String query = "1";
        List<String> fields = Arrays.asList("nome", "profissional");

        List<Contato> contatosMock = Arrays.asList(
                Contato.builder().nome("Contato 1").profissional(profissional).build(),
                Contato.builder().nome("Contato 2").profissional(profissional).build()
        );

        when(filterUtils.filterObjectsByText(anyList(), anyString(), any()))
                .thenReturn(List.of(contatosMock.get(0)));
        when(filterUtils.filterFields(anyList(), anyList()))
                .thenReturn(List.of(contatosMock.get(0)));
        when(contatoRepository.findAll()).thenReturn(contatosMock);

        List<ContatoResponseDto> contatosResponseDtos = contatoService.searchAndFilterContatos(query, fields);

        assertNotNull(contatosResponseDtos);
        assertNotEquals(contatosMock.size(), contatosResponseDtos.size());
        assertEquals(contatosMock.get(0).getNome(), contatosResponseDtos.get(0).getNome());
        assertEquals(contatosMock.get(0).getProfissional().getId(), contatosResponseDtos.get(0).getProfissional().getId());
        assertNull(contatosResponseDtos.get(0).getCreatedDate());
    }

    @Test
    void testGetContatoById_ContatoEncontrado() {
        when(contatoRepository.findById(anyLong())).thenReturn(Optional.of(contato));

        var contatoEncontrado = contatoService.getById(1L);

        assertNotNull(contatoEncontrado);
        assertEquals(contatoEncontrado.getId(), contato.getId());
    }

    @Test
    void testGetContatoById_ContatoNaoEncontrado() {
        assertThrows(ResourceNotFoundException.class, () -> contatoService.getById(1L));
    }

    @Test
    void testInsertContato() {
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        var contatoSalvo = contatoService.insert(contatoRequestDto);

        assertNotNull(contatoSalvo);
        assertEquals(ContatoResponseDto.class, contatoSalvo.getClass());
        assertEquals(contatoRequestDto.getNome(), contatoSalvo.getNome());
        assertEquals(contatoRequestDto.getProfissional().getId(), contatoSalvo.getProfissional().getId());
    }

    @Test
    void testUpdateContato_ContatoEncontrado() {
        when(contatoRepository.findById(anyLong())).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        contatoService.update(contato.getId(), contatoRequestDto);

        verify(contatoRepository, times(1)).save(contato);
        verify(contatoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateContato_ContatoNaoEncontrado() {
        assertThrows(ResourceNotFoundException.class, () -> contatoService.update(1L, contatoRequestDto));
    }

    @Test
    void testDeleteContato() {
        contatoService.delete(1L);

        verify(contatoRepository, times(1)).deleteById(1L);
    }

    private Contato buildContato(Long id, String nome, Profissional profissional, Date createdDate) {
        return Contato.builder()
                .id(id)
                .nome(nome)
                .profissional(profissional)
                .createdDate(createdDate)
                .build();
    }

    private ContatoRequestDto buildContatoRequestDto(String nome, Profissional profissional) {
        return ContatoRequestDto.builder()
                .nome(nome)
                .profissional(profissional)
                .build();
    }

    private Profissional buildProfissional() {
        return Profissional.builder()
                .id(1L)
                .nome("Profissional Teste")
                .cargo(Cargo.DESENVOLVEDOR)
                .ativo(true)
                .createdDate(new Date())
                .build();
    }

}
