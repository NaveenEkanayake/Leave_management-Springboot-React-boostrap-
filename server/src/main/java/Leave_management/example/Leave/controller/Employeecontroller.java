package Leave_management.example.Leave.controller;

import Leave_management.example.Leave.Email.UpdateEmployeeEmail;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import java.security.SecureRandom;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/employee")
public class Employeecontroller {
    private EmployeeService employeeservice;
    private EmployeeRepository employeeRepository;
    private AuthEmployeeEmail authEmployeeEmail;
    private UpdateEmployeeEmail updateEmployeesendemail;
    private AuthEmployeeservice authEmployeeservice;
    private static final Logger logger = LoggerFactory.getLogger(Employeecontroller.class);

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addEmployee")
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

    // Get Employee by ID
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable("id") Long id) {
        EmployeeDto employeeDto = employeeservice.getEmployeeByID(id);

        Map<String, Object> response = new HashMap<>();
        response.put("data", employeeDto);
        response.put("message", "Employee Data Retrieved Successfully!");

        return ResponseEntity.ok(response);
    }

    // Get All Employees
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAllEmployees")
    public ResponseEntity<Map<String, Object>> getAllEmployees() {
        List<EmployeeDto> employees = employeeservice.getAllEmployees();

        // Exclude Admins from the list
        employees = employees.stream()
                .filter(employee -> !employee.getRole().equals("ROLE_ADMIN"))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("data", employees);
        response.put("message", "All Employee Data Retrieved Successfully!");
        return ResponseEntity.ok(response);
    }

    //update EMployee
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/UpdateEmployee/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody EmployeeDto updatedEmployee) {

        try {
            // Assuming the service method is properly updating the employee and returning the updated EmployeeDto
            EmployeeDto employeeDto = employeeservice.updateEMployee(id, updatedEmployee);

            // Prepare the response map
            Map<String, Object> response = new HashMap<>();
            response.put("data", employeeDto);
            response.put("message", "Employee updated successfully!");

            // Prepare the email content
            String subject = "Your Information has been Updated in the Leave Management System";
            String body = String.format(
                    "Hello %s,\n\n" +
                            "Your information has been successfully updated in our Leave Management System. Here are the updated details:\n\n" +
                            "Name: %s %s\n" +
                            "Role: %s\n" +
                            "Email: %s\n\n" +
                            "If you have any questions or require further assistance, please do not hesitate to reach out.\n\n" +
                            "Best regards,\n" +
                            "Leave Management Team",
                    employeeDto.getFirstName(),
                    employeeDto.getFirstName(),
                    employeeDto.getLastName(),
                    employeeDto.getRole(),
                    employeeDto.getEmail());

            // Send the update email to the employee using the autowired service
            updateEmployeesendemail.sendUpdatedEmail(employeeDto.getEmail(), subject, body);

            // Return the response
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log the error and return an internal server error response
            logger.error("Error occurred while updating employee: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Error occurred while updating employee. Please try again later."));
        }
    }

    // Delete Employee
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("deleteEmployee/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable("id") Long id) {
        try {
            EmployeeDto employeeDto = employeeservice.getEmployeeByID(id);
            authEmployeeservice.DeleteAuthEmployee(id);
            employeeservice.deleteEmployee(id);
            Map<String, Object> response = new HashMap<>();
            response.put("id", employeeDto.getId());
            response.put("firstName", employeeDto.getFirstName());
            response.put("lastName", employeeDto.getLastName());
            response.put("role", employeeDto.getRole());
            response.put("message", "Employee and their credentials deleted successfully!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred while deleting employee: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Error occurred while deleting the employee. Please try again later."));
        }
    }



}