package com.endava.webhw.service;

import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.model.Employee;
import com.endava.webhw.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee with id: "+id+" not found."));
    }

    @Transactional
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(long id, Employee employee) {
        Employee updatedEmployee = findById(id);

        //have to optimize
        updatedEmployee.setFirstName(employee.getFirstName());
        updatedEmployee.setLastName(employee.getLastName());
        updatedEmployee.setDepartment(employee.getDepartment());
        updatedEmployee.setEmail(employee.getEmail());
        updatedEmployee.setPhoneNumber(employee.getPhoneNumber());
        updatedEmployee.setSalary(employee.getSalary());

        return updatedEmployee;
    }
}
