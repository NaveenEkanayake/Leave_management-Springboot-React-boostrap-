package Leave_management.example.Leave.controller;

import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.AuthEmployee;
import Leave_management.example.Leave.service.AuthEmployeeservice;
import Leave_management.example.Leave.util.Jwtutil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthEmployeecontroller {

    private final AuthEmployeeservice authEmployeeservice;
    private final Jwtutil jwtutil;
    private final PasswordEncoder passwordEncoder;

    public AuthEmployeecontroller(AuthEmployeeservice authEmployeeservice, Jwtutil jwtutil,
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
            if (passwordEncoder.matches(authEmployeeDto.getPassword(), employee.getPassword())) {
                String token = jwtutil.generateToken(employee.getEmail());
                return ResponseEntity.ok(Map.of(
                        "email", employee.getEmail(),
                        "name", employee.getFirstName() + " " + employee.getLastName(),
                        "token", token

                ));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid password"));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Employee not found"));
        }
    }

}
