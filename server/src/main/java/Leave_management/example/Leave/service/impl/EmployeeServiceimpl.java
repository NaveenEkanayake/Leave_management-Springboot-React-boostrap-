package Leave_management.example.Leave.service.impl;

import Leave_management.example.Leave.dto.EmployeeDto;
import Leave_management.example.Leave.entity.Employee;
import Leave_management.example.Leave.mapper.Employeemapper;
import Leave_management.example.Leave.repository.EmployeeRepository;
import Leave_management.example.Leave.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceimpl implements EmployeeService {

    //Repository
    private EmployeeRepository employeeRepository;

    //create Employees
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = Employeemapper.mapToEmployee(employeeDto); // making this into employee jpa entity
        Employee newEmployee= employeeRepository.save(employee); // creating new employee
        return Employeemapper.mapToEmployeeDto(newEmployee);// returning the employee back to the employee mapper
    }
}
