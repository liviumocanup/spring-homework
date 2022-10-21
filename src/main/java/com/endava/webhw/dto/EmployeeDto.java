package com.endava.webhw.dto;

import com.endava.webhw.model.Employee;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    @JsonAlias("first-name")
    private String firstName;

    @JsonAlias("last-name")
    private String lastName;

    private Long department;

    private String email;

    @JsonAlias("phone-number")
    private String phoneNumber;

    private Double salary;

    public Employee toEmployee() {
        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .email(email)
                .phoneNumber(phoneNumber)
                .salary(salary)
                .build();
    }
}
