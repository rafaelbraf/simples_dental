package com.simplesdental.testepratico.profissionais.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.isNull;

@Entity
@Table(name = "contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contato {

    @Serial
    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    public ContatoResponseDto toContatoResponseDto() {
        return ContatoResponseDto.builder()
                .id(id)
                .nome(nome)
                .createdDate(createdDate)
                .profissional(buildProfissional())
                .build();
    }

    private Profissional buildProfissional() {
        if (isNull(profissional)) {
            return null;
        }

        return Profissional.builder()
            .id(profissional.getId())
            .nome(profissional.getNome())
            .nascimento(profissional.getNascimento())
            .createdDate(profissional.getCreatedDate())
            .ativo(profissional.isAtivo())
            .build();
    }

}
