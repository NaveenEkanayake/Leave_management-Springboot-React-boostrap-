package Leave_management.example.Leave.service.impl;

import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.AuthEmployee;
import Leave_management.example.Leave.exception.ResourceNotFoundException;
import Leave_management.example.Leave.mapper.AuthEmployeemapper;
import Leave_management.example.Leave.repository.AuthEmployeerepository;
import Leave_management.example.Leave.service.AuthEmployeeservice;
import Leave_management.example.Leave.util.Jwtutil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthEmployeeimpl implements AuthEmployeeservice {

    private final AuthEmployeerepository authEmployeerepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwtutil jwtutil;

    @Override
    public AuthEmployeeDto SignEmployee(AuthEmployeeDto authEmployeeDto) {
        AuthEmployee authEmployee = AuthEmployeemapper.mapToAuthEmployee(authEmployeeDto);

        // Encode the password
        String encodedPassword = passwordEncoder.encode(authEmployee.getPassword());
        authEmployee.setPassword(encodedPassword);
        AuthEmployee newEmployee = authEmployeerepository.save(authEmployee);

        // Map back to DTO and return
        return AuthEmployeemapper.mapToAuthEmployeeDto(newEmployee);
    }

    @Override
    public Optional<AuthEmployee> findByEmail(String email) {
        return authEmployeerepository.findByEmail(email);
    }

    @Override
    public void DeleteAuthEmployee(Long id) {
        AuthEmployee employee = authEmployeerepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not exist with the given ID.")
        );
        authEmployeerepository.deleteById(id);
    }
}
