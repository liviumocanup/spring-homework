package com.endava.webhw.dto;

import com.endava.webhw.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private String name;

    private String location;

    public Department toDepartment() {
        return Department.builder()
                .name(name)
                .location(location)
                .build();
    }
}
