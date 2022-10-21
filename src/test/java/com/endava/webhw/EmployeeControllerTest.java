package com.endava.webhw;

import com.endava.webhw.controller.DepartmentController;
import com.endava.webhw.controller.EmployeeController;
import com.endava.webhw.dto.EmployeeDto;
import com.endava.webhw.exception.EntityNotFoundException;
import com.endava.webhw.model.Employee;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

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

    private Employee expectedEmployee;
    private String jsonEmployee;
    private ResponseEntity<?> r;

    @BeforeEach
    public void setUp() {
        expectedEmployee = new EmployeeDto("John", "Doe", 1L, "jdoe@mail.com",
                "+3738404404", 500.0).toEmployee();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonEmployee = objectMapper.writeValueAsString(new EmployeeDto("John", "Doe",
                    1L, "jdoe@mail.com", "+3738404404", 500.0));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        r = ResponseEntity.status(HttpStatus.CREATED)
                .header("Location /employees/1")
                .build();
    }

    @Test
    void testFindAllShouldReturnEmptyListWhenRepositoryEmpty() throws Exception {
        doReturn(new ArrayList<Employee>()).when(employeeService).findAll();

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").doesNotExist());
    }

    @Test
    void testFindByIdShouldThrowExceptionWhenThereIsNoSuchId() throws Exception {
        doThrow(new EntityNotFoundException("Employee with id 1 not found")).when(employeeService).findById(1L);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Employee with id 1 not found"))
                .andExpect(jsonPath("error").value("Entity not found"));
    }

    @Test
    void testFindByIdShouldThrowExceptionForNonNumericIdProvided() throws Exception {
        doThrow(MethodArgumentTypeMismatchException.class).when(employeeService).findById(any());

        mockMvc.perform(get("/employees/a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Parameter: id, has invalid value: a"))
                .andExpect(jsonPath("error").value("Bad parameter value"));
    }

    @Test
    void testFindByIdObjectFieldsShouldMatchWhenIdFound() throws Exception {
        doReturn(expectedEmployee).when(employeeService).findById(1L);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedEmployee.getLastName()))
                .andExpect(jsonPath("$.department").value(expectedEmployee.getDepartment()))
                .andExpect(jsonPath("$.email").value(expectedEmployee.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(expectedEmployee.getPhoneNumber()))
                .andExpect(jsonPath("$.salary").value(expectedEmployee.getSalary()));
    }

    @Test
    void testCreateExpectedUriShouldBeCorrectAndObjectFieldsMatchIfTheProvidedBodyIsValid() throws Exception {
        doReturn(expectedEmployee).when(employeeService).create(any(Employee.class));

        mockMvc.perform(
                        post("/employees")
                                .content(jsonEmployee)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedEmployee.getLastName()))
                .andExpect(jsonPath("$.department").value(expectedEmployee.getDepartment()))
                .andExpect(jsonPath("$.email").value(expectedEmployee.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(expectedEmployee.getPhoneNumber()))
                .andExpect(jsonPath("$.salary").value(expectedEmployee.getSalary()))
                .andReturn().getResponse().getHeaders(r.getHeaders().toString());
    }

    @Test
    void testUpdateShouldThrowExceptionIfIdNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Employee with id 1 not found")).when(employeeService).update(eq(1L), any(Employee.class));

        mockMvc.perform(
                        put("/employees/1")
                                .content(jsonEmployee)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Employee with id 1 not found"))
                .andExpect(jsonPath("error").value("Entity not found"));
    }

    @Test
    void testUpdateExpectedUriShouldBeCorrectAndObjectFieldsMatchIfTheProvidedBodyIsValid() throws Exception {
        expectedEmployee.setId(1L);

        doReturn(expectedEmployee).when(employeeService).update(eq(1L), any(Employee.class));

        mockMvc.perform(
                        put("/employees/1")
                                .content(jsonEmployee)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedEmployee.getLastName()))
                .andExpect(jsonPath("$.department").value(expectedEmployee.getDepartment()))
                .andExpect(jsonPath("$.email").value(expectedEmployee.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(expectedEmployee.getPhoneNumber()))
                .andExpect(jsonPath("$.salary").value(expectedEmployee.getSalary()))
                .andReturn().getResponse().getHeaders(r.getHeaders().toString());
    }
}
