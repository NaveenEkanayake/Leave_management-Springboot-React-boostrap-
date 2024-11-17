package Leave_management.example.Leave.service;

import Leave_management.example.Leave.dto.EmployeeDto;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto); // to create the employee first we create the method as Dto and
                                                         // Pass the Dto as Parameters

}
