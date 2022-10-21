package com.endava.webhw;

import com.endava.webhw.controller.DepartmentController;
import com.endava.webhw.controller.EmployeeController;
import com.endava.webhw.dto.DepartmentDto;
import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.model.Department;
import com.endava.webhw.service.DepartmentService;
import com.endava.webhw.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private DepartmentController departmentController;

    private Department expectedDepartment;
    private String jsonDepartment;
    private ResponseEntity<?> r;

    @BeforeEach
    public void setUp() {
        expectedDepartment = new DepartmentDto("IT", "Chisinau").toDepartment();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonDepartment = objectMapper.writeValueAsString(new DepartmentDto("IT", "Chisinau"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        r = ResponseEntity.status(HttpStatus.CREATED)
                .header("Location /departments/1")
                .build();
    }

    @Test
    void testFindAllShouldReturnEmptyListWhenRepositoryEmpty() throws Exception {
        doReturn(new ArrayList<Department>()).when(departmentService).findAll();

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").doesNotExist());
    }

    @Test
    void testFindByIdShouldThrowExceptionWhenThereIsNoSuchId() throws Exception {
        doThrow(new EntityNotFoundException("Department with id 1 not found")).when(departmentService).findById(1L);

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Department with id 1 not found"))
                .andExpect(jsonPath("error").value("Entity not found"));
    }

    @Test
    void testFindByIdThrowExceptionForNonNumericIdProvided() throws Exception {
        doThrow(MethodArgumentTypeMismatchException.class).when(departmentService).findById(any());

        mockMvc.perform(get("/departments/a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Parameter: id, has invalid value: a"))
                .andExpect(jsonPath("error").value("Bad parameter value"));
    }

    @Test
    void testFindByIdObjectFieldsShouldMatchWhenIdFound() throws Exception {
        doReturn(expectedDepartment).when(departmentService).findById(1L);

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedDepartment.getId()))
                .andExpect(jsonPath("$.name").value(expectedDepartment.getName()))
                .andExpect(jsonPath("$.location").value(expectedDepartment.getLocation()));
    }

    @Test
    void testCreateExpectedUriShouldBeCorrectAndObjectFieldsMatchIfTheProvidedBodyIsValid() throws Exception {
        doReturn(expectedDepartment).when(departmentService).create(any(Department.class));

        mockMvc.perform(
                        post("/departments")
                                .content(jsonDepartment)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedDepartment.getId()))
                .andExpect(jsonPath("$.name").value(expectedDepartment.getName()))
                .andExpect(jsonPath("$.location").value(expectedDepartment.getLocation()))
                .andReturn().getResponse().getHeaders(r.getHeaders().toString());
    }

    @Test
    void testUpdateShouldThrowExceptionIfIdNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Department with id 1 not found")).when(departmentService).update(eq(1L), any(Department.class));

        mockMvc.perform(
                        put("/departments/1")
                                .content(jsonDepartment)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Department with id 1 not found"))
                .andExpect(jsonPath("error").value("Entity not found"));
    }

    @Test
    void testUpdateExpectedUriShouldBeCorrectAndObjectFieldsMatchIfTheProvidedBodyIsValid() throws Exception {
        expectedDepartment.setId(1L);

        doReturn(expectedDepartment).when(departmentService).update(eq(1L), any(Department.class));

        mockMvc.perform(
                        put("/departments/1")
                                .content(jsonDepartment)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedDepartment.getId()))
                .andExpect(jsonPath("$.name").value(expectedDepartment.getName()))
                .andExpect(jsonPath("$.location").value(expectedDepartment.getLocation()))
                .andReturn().getResponse().getHeaders(r.getHeaders().toString());
    }
}
