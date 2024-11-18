package Leave_management.example.Leave.controller;

import Leave_management.example.Leave.dto.AdminloginDto;
import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.entity.AuthEmployee;
import Leave_management.example.Leave.service.Adminloginservice;
import Leave_management.example.Leave.util.Jwtutil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class Adminlogincontroller {

    private final Adminloginservice adminloginservice;
    private final Jwtutil jwtutil;
    private final PasswordEncoder passwordEncoder;

    public Adminlogincontroller(Adminloginservice adminloginservice, Jwtutil jwtutil, PasswordEncoder passwordEncoder) {
        this.adminloginservice = adminloginservice;
        this.jwtutil = jwtutil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String adminSignup(@RequestBody AdminloginDto adminloginDto) {
        AdminloginDto savedAdmin = adminloginservice.adminsignup(adminloginDto);
        return "Admin created with ID: " + savedAdmin.getId();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdminloginDto adminloginDto) {
        // Try to find admin by email
        Optional<Adminlogin> adminloginOpt = adminloginservice.findByEmail(adminloginDto.getEmail());
        if (adminloginOpt.isPresent()) {
            Adminlogin adminlogin = adminloginOpt.get();

            if (passwordEncoder.matches(adminloginDto.getPassword(), adminlogin.getPassword())) {
                String token = jwtutil.generateToken(adminlogin.getEmail());

                return ResponseEntity.ok(Map.of(
                        "Adminname", adminlogin.getFullname(),
                        "email", adminlogin.getEmail(),
                        "admintoken", token

                ));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid password"));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Admin not found"));
        }
    }
}
