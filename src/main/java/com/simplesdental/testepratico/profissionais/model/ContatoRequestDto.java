package com.simplesdental.testepratico.profissionais.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoRequestDto {

    @NotEmpty(message = "Nome não pode ser nulo ou vazio")
    private String nome;

    @NotNull(message = "Profissional não pode ser nulo")
    private Profissional profissional;

    private Date createdDate = new Date();

    public Contato toContato() {
        return Contato.builder()
                .nome(nome)
                .createdDate(createdDate)
                .profissional(profissional)
                .build();
    }

}
