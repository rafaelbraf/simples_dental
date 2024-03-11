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
public class ProfissionalRequestDto {

    private String nome;
    private Cargo cargo;
    private Date nascimento;
    private Date createdDate = new Date();
    private boolean ativo = true;

    public Profissional toProfissional() {
        return Profissional.builder()
                .nome(nome)
                .nascimento(nascimento)
                .cargo(cargo)
                .createdDate(createdDate)
                .ativo(ativo)
                .build();
    }

}
