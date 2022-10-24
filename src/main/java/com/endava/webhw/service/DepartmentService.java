package com.endava.webhw.service;

import com.endava.webhw.dto.DepartmentDto;
import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.model.Department;
import com.endava.webhw.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<DepartmentDto> findAll() {
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        List<Department> allDeparmentsList = departmentRepository.findAll();

        for (Department d : allDeparmentsList) {
            departmentDtos.add(new DepartmentDto(d));
        }

        return departmentDtos;
    }

    @Transactional(readOnly = true)
    public DepartmentDto findById(Long id) {
        Department foundDepartment = findByIdForDepartment(id);
        return new DepartmentDto(foundDepartment);
    }

    private Department findByIdForDepartment(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department with id: " + id + " not found."));
    }

    @Transactional
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(long id, Department department) {
        Department updatedDepartment = findByIdForDepartment(id);

        updatedDepartment.setName(department.getName());
        updatedDepartment.setLocation(department.getLocation());

        return updatedDepartment;
    }
}
