package Leave_management.example.Leave.mapper;

import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.AuthEmployee;

public class AuthEmployeemapper {
    public static AuthEmployeeDto mapToAuthEmployeeDto(AuthEmployee authEmployee) {
        return new AuthEmployeeDto(
                authEmployee.getId(),
                authEmployee.getFirstName(),
                authEmployee.getLastName(),
                authEmployee.getEmail(),
                null
        );
    }
    public static AuthEmployee mapToAuthEmployee(AuthEmployeeDto authEmployeeDto) {
        AuthEmployee authEmployee = new AuthEmployee();
        authEmployee.setFirstName(authEmployeeDto.getFirstName());
        authEmployee.setLastName(authEmployeeDto.getLastName());
        authEmployee.setEmail(authEmployeeDto.getEmail());
        authEmployee.setPassword(authEmployeeDto.getPassword());
        return authEmployee;
    }
}
