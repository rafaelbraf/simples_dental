package com.simplesdental.testepratico.profissionais.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContatoResponseDto {

    private Long id;
    private String nome;
    private Date createdDate;
    private Profissional profissional;

}
