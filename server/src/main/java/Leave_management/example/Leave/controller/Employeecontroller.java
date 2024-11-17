package Leave_management.example.Leave.controller;

import Leave_management.example.Leave.dto.EmployeeDto;
import Leave_management.example.Leave.entity.Employee;
import Leave_management.example.Leave.service.EmployeeService;
import Leave_management.example.Leave.repository.EmployeeRepository;
import Leave_management.example.Leave.Email.AuthEmployeeEmail;
import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.service.AuthEmployeeservice;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/AddEmployee")
public class Employeecontroller {
    private EmployeeService employeeservice;
    private EmployeeRepository employeeRepository;
    private AuthEmployeeEmail authEmployeeEmail;
    private AuthEmployeeservice authEmployeeservice;
    private static final Logger logger = LoggerFactory.getLogger(Employeecontroller.class);

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            boolean employeeExists = employeeRepository.existsByFirstNameAndLastName(
                    employeeDto.getFirstName(),
                    employeeDto.getLastName()) ||
                    employeeRepository.existsByEmail(employeeDto.getEmail());

            if (employeeExists) {
                logger.warn("Employee already exists with first name: {}, last name: {}, or email: {}",
                        employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getEmail());
                return new ResponseEntity<>("Employee already exists with the provided details.", HttpStatus.CONFLICT);
            }

            // Generate a random password
            String generatedPassword = generateRandomPassword();
            AuthEmployeeDto authEmployeeDto = new AuthEmployeeDto(
                    employeeDto.getId(),
                    employeeDto.getFirstName(),
                    employeeDto.getLastName(),
                    employeeDto.getEmail(),
                    generatedPassword);

            Employee employee = new Employee(
                    employeeDto.getId(),
                    employeeDto.getFirstName(),
                    employeeDto.getLastName(),
                    employeeDto.getEmail(),
                    employeeDto.getRole());

            Employee savedEmployee = employeeRepository.save(employee);
            String employeeId = String.valueOf(authEmployeeservice.SignEmployee(authEmployeeDto));
            String subject = "Welcome to Our Leave Management System";
            String body = String.format(
                    "Hello %s,\n\n" +
                            "You have been added to our Leave Management System as a %s. Here are your login credentials:\n\n"
                            +
                            "Email: %s\n" +
                            "Password: %s\n\n" +
                            "If you have any questions or require further assistance, please do not hesitate to reach out.\n\n"
                            +
                            "Best regards,\n" +
                            "Leave_Management Team",
                    employeeDto.getFirstName(), employeeDto.getRole(), employeeDto.getEmail(), generatedPassword);

            authEmployeeEmail.sendEmail(employeeDto.getEmail(), subject, body);

            logger.info("Employee created successfully with ID: {}", employeeId);
            return new ResponseEntity<>(Map.of("message", "Employee created successfully", "data", employeeId),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error occurred while creating employee: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error occurred while creating employee. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
