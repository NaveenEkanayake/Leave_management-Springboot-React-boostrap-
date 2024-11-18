package Leave_management.example.Leave.service;

import Leave_management.example.Leave.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto); // to create the employee first we create the method as Dto and
                                                         // Pass the Dto as Parameters
EmployeeDto getEmployeeByID(Long Id);

List<EmployeeDto> getAllEmployees();
EmployeeDto updateEMployee(Long Id,EmployeeDto updatedemployee);

void deleteEmployee(Long Id);
}
