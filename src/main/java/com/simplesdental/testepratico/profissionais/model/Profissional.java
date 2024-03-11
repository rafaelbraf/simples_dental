package com.simplesdental.testepratico.profissionais.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "profissional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profissional implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private Cargo cargo;

    @Column
    private Date nascimento;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "ativo")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean ativo;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Contato> contatos;

}
