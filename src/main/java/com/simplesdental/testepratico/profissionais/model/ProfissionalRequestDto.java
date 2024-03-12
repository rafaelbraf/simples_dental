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
public class ProfissionalRequestDto {

    @NotEmpty(message = "Nome não pode ser vazio")
    private String nome;

    @NotNull(message = "Cargo não pode ser nulo")
    private Cargo cargo;

    @NotNull(message = "Nascimento não pode ser nulo")
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
