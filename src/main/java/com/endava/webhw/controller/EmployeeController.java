package com.endava.webhw.controller;

import com.endava.webhw.dto.EmployeeDto;
import com.endava.webhw.model.Employee;
import com.endava.webhw.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("{id}")
    public Employee findById(@PathVariable long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.create(employeeDto.toEmployee());

        URI uri = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .build(createdEmployee.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(createdEmployee);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.update(id, employeeDto.toEmployee());

        URI uri = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .build(updatedEmployee.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .location(uri)
                .body(updatedEmployee);
    }
}
