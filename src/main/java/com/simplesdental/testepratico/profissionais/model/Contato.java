package com.simplesdental.testepratico.profissionais.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.Date;

@Entity
@Table(name = "contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
