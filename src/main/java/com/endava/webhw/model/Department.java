package com.endava.webhw.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Department {
    @Id
    @SequenceGenerator(name = "dep_seq", sequenceName = "department_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dep_seq")
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(name = "location")
    private String location;
}
