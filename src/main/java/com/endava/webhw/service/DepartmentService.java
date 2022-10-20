package com.endava.webhw.service;

import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.model.Department;
import com.endava.webhw.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department with id: "+id+" not found."));
    }

    @Transactional
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(long id, Department department) {
        Department updatedDepartment = findById(id);

        updatedDepartment.setName(department.getName());
        updatedDepartment.setLocation(department.getLocation());

        return updatedDepartment;
    }
}
