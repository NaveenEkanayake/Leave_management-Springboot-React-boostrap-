package Leave_management.example.Leave.controller;

import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.AuthEmployee;
import Leave_management.example.Leave.service.AuthEmployeeservice;
import Leave_management.example.Leave.util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
public class AuthEmployeecontroller {

    private final AuthEmployeeservice authEmployeeservice;
    private final JwtService jwtutil;
    private final PasswordEncoder passwordEncoder;

    public AuthEmployeecontroller(AuthEmployeeservice authEmployeeservice, JwtService jwtutil,
            PasswordEncoder passwordEncoder) {
        this.authEmployeeservice = authEmployeeservice;
        this.jwtutil = jwtutil;
        this.passwordEncoder = passwordEncoder;
    }

    public String AuthEmployee(@RequestBody AuthEmployeeDto authEmployeeDto) {
        String id = String.valueOf(authEmployeeservice.SignEmployee(authEmployeeDto));
        return id;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthEmployeeDto authEmployeeDto) {
        Optional<AuthEmployee> employeeOpt = authEmployeeservice.findByEmail(authEmployeeDto.getEmail());

        if (employeeOpt.isPresent()) {
            AuthEmployee employee = employeeOpt.get();

            // Check if the provided password matches the stored password
            if (passwordEncoder.matches(authEmployeeDto.getPassword(), employee.getPassword())) {
                // Prepare extra claims
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("firstName", employee.getFirstName());
                extraClaims.put("lastName", employee.getLastName());

                // Generate token with email and extra claims
                String token = jwtutil.generateToken(employee.getEmail(), extraClaims);

                // Return response with employee details and token
                return ResponseEntity.ok(Map.of(
                        "email", employee.getEmail(),
                        "name", employee.getFirstName() + " " + employee.getLastName(),
                        "token", token
                ));
            } else {
                // Return unauthorized status if the password doesn't match
                return ResponseEntity.status(401).body(Map.of("message", "Invalid password"));
            }
        } else {
            // Return not found status if employee doesn't exist
            return ResponseEntity.status(404).body(Map.of("message", "Employee not found"));
        }
    }

}

