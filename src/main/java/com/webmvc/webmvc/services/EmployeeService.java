package com.webmvc.webmvc.services;

import com.webmvc.webmvc.dto.EmployeeDTO;
import com.webmvc.webmvc.entities.EmployeeEntity;
import com.webmvc.webmvc.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeDTO getEmployeeByID(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElse(null);
        if (employeeEntity == null) return null;
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        EmployeeEntity toSaveEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        EmployeeEntity savedEntity = employeeRepository.save(toSaveEntity);
        return modelMapper.map(savedEntity, EmployeeDTO.class);
    }

    public boolean deleteEmployee(Long id) {
        boolean exists = employeeExistsByID(id);
        if (!exists) return false;
        employeeRepository.deleteById(id);
        return true;
    }

    public EmployeeDTO updateEmployeeById(EmployeeDTO employeeDTO, Long id) {
        if (!employeeExistsByID(id)) return null;
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(id);
        EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEntity, EmployeeDTO.class);
    }

    public EmployeeDTO updatePartialEmployeeById(Long employeeID, Map<String, Object> updates) {
        boolean exists = employeeExistsByID(employeeID);
        if (!exists) return null;
        EmployeeEntity entity = employeeRepository.findById(employeeID).get();
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(EmployeeEntity.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, entity, value);
        });
        return modelMapper.map(employeeRepository.save(entity), EmployeeDTO.class);
    }

    public boolean employeeExistsByID(Long id) {
        return employeeRepository.existsById(id);
    }

}
