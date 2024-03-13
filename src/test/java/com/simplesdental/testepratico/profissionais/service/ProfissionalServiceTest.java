package com.simplesdental.testepratico.profissionais.service;

import com.simplesdental.testepratico.profissionais.exception.ResourceNotFoundException;
import com.simplesdental.testepratico.profissionais.model.Cargo;
import com.simplesdental.testepratico.profissionais.model.Profissional;
import com.simplesdental.testepratico.profissionais.model.ProfissionalRequestDto;
import com.simplesdental.testepratico.profissionais.repository.ProfissionalRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfissionalServiceTest {

    @InjectMocks
    private ProfissionalService profissionalService;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private FilterUtils<Profissional> filterUtils;

    private Profissional profissional;
    private ProfissionalRequestDto profissionalRequestDto;

    @BeforeEach
    void setup() {
        profissional = buildProfissional();
        profissionalRequestDto = buildProfissionalRequestDto(profissional.getNome(), profissional.getCargo(), profissional.getNascimento());
    }

    @Test
    public void testSearchAndFilterProfissionais() {
        String query = "desenvolvedor";
        List<String> fields = Arrays.asList("nome", "cargo");

        List<Profissional> profissionaisMock = Arrays.asList(
                Profissional.builder().nome("Profissional Teste 1").cargo(Cargo.DESENVOLVEDOR).build(),
                Profissional.builder().nome("Profissional Teste 2").cargo(Cargo.DESIGNER).build()
        );

        when(filterUtils.filterObjectsByText(anyList(), anyString(), any()))
                .thenReturn(List.of(profissionaisMock.get(0)));
        when(filterUtils.filterFields(anyList(), anyList()))
                .thenReturn(List.of(profissionaisMock.get(0)));
        when(profissionalRepository.findAll()).thenReturn(profissionaisMock);

        List<Profissional> profissionaisFiltrados = profissionalService.searchAndFilter(query, fields);

        assertNotNull(profissionaisFiltrados);
        assertNotEquals(profissionaisMock.size(), profissionaisFiltrados.size());
        assertEquals(profissionaisMock.get(0).getNome(), profissionaisFiltrados.get(0).getNome());
        assertEquals(profissionaisMock.get(0).getCargo(), profissionaisFiltrados.get(0).getCargo());
        assertNull(profissionaisFiltrados.get(0).getNascimento());
    }

    @Test
    void testGetProfissionalById_ProfissionalEncontrado() {
        when(profissionalRepository.findById(anyLong())).thenReturn(Optional.of(profissional));

        var profissionalEncontrado = profissionalService.findById(1L);

        assertNotNull(profissionalEncontrado);
        assertEquals(profissional.getId(), profissionalEncontrado.getId());
    }

    @Test
    void testGetProfissionalById_ProfissionalNaoEncontrado() {
        assertThrows(ResourceNotFoundException.class, () -> profissionalService.findById(1L));
    }

    @Test
    void testInsertProfissional() {
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        var profissionalSalvo = profissionalService.insert(profissionalRequestDto);

        assertNotNull(profissionalSalvo);
        assertEquals(profissionalSalvo.getNome(), profissionalSalvo.getNome());
    }

    @Test
    void testUpdateProfissional_ProfissionalEncontrado() {
       when(profissionalRepository.findById(anyLong())).thenReturn(Optional.of(profissional));
       when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

       profissionalService.update(profissional.getId(), profissionalRequestDto);

       verify(profissionalRepository, times(1)).findById(1L);
       verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    void testUpdateProfissional_ProfissionalNaoEncontrado() {
        assertThrows(ResourceNotFoundException.class, () -> profissionalService.update(profissional.getId(), profissionalRequestDto));
    }

    @Test
    void testDeleteProfissional() {
        when(profissionalRepository.findById(anyLong())).thenReturn(Optional.of(profissional));

        profissionalService.delete(1L);

        verify(profissionalRepository, times(1)).save(profissional);
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

    private ProfissionalRequestDto buildProfissionalRequestDto(String nome, Cargo cargo, Date nascimento) {
        return ProfissionalRequestDto.builder()
                .nome(nome)
                .cargo(cargo)
                .nascimento(nascimento)
                .build();
    }

}
