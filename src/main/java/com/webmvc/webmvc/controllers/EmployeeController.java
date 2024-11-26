package com.webmvc.webmvc.controllers;

import com.webmvc.webmvc.dto.EmployeeDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    @GetMapping(path = "/{employeeId}")
    public EmployeeDTO getEmployeeByID(@PathVariable(name = "employeeId") Long id) {
        return new EmployeeDTO(id, "Harsh", "harsh@gmail.com", 26, LocalDate.of(2021, 01, 18), true);
    }

    @GetMapping(path = "")
    public String getAllEmployees(@RequestParam(required = false) Integer age, @RequestParam(required = false) String sortBy) {
        return "Hi age: " + age + " " + sortBy;
    }

}
