package com.endava.webhw.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee")
@ToString
public class Employee {
    @Id
    @SequenceGenerator(name = "emp_seq", sequenceName = "employee_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "department")
    private Long department;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "salary")
    private Double salary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;
        return id != null && Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
