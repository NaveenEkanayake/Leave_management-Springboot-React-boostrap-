package Leave_management.example.Leave.service.impl;

import Leave_management.example.Leave.dto.EmployeeDto;
import Leave_management.example.Leave.entity.Employee;
import Leave_management.example.Leave.exception.ResourceNotFoundException;
import Leave_management.example.Leave.mapper.Employeemapper;
import Leave_management.example.Leave.repository.EmployeeRepository;
import Leave_management.example.Leave.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public EmployeeDto getEmployeeByID(Long Id) {
      Employee employee = employeeRepository.findById(Id)
               .orElseThrow(() -> new ResourceNotFoundException("Employee is not exist with given id "));

      return Employeemapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
       List<Employee> employees= employeeRepository.findAll();

       return employees.stream().map((employee -> Employeemapper.mapToEmployeeDto(employee)))
               .collect(Collectors.toList());

    }

    @Override
    public EmployeeDto updateEMployee(Long Id, EmployeeDto updatedemployee) {
     Employee employee=  employeeRepository.findById(Id).orElseThrow(() -> new ResourceNotFoundException("Employee is not Exist with Given Id "));
       employee.setFirstName(updatedemployee.getFirstName());
       employee.setLastName(updatedemployee.getLastName());
       employee.setEmail(updatedemployee.getEmail());
       employee.setRole(updatedemployee.getRole());
       Employee updatedEmployeeObj= employeeRepository.save(employee);

       return Employeemapper.mapToEmployeeDto(updatedEmployeeObj);

    }

    @Override
    public void deleteEmployee(Long Id) {

        Employee employee = employeeRepository.findById(Id).orElseThrow(
                () -> new ResourceNotFoundException("Employee is not exist with given ID ")
        );

        employeeRepository.deleteById(Id);

    }
}

