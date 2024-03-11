package com.simplesdental.testepratico.profissionais.model;

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

    private String nome;
    private Date createdDate = new Date();
    private Profissional profissional;

    public Contato toContato() {
        return Contato.builder()
                .nome(nome)
                .createdDate(createdDate)
                .profissional(profissional)
                .build();
    }

}
