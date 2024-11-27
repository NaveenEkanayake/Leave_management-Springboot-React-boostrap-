package Leave_management.example.Leave.controller;

import Leave_management.example.Leave.dto.AdminloginDto;
import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.service.Adminloginservice;
import Leave_management.example.Leave.util.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static javax.crypto.Cipher.SECRET_KEY;

@RestController
@RequestMapping("/api/admin")
public class Adminlogincontroller {

    private final Adminloginservice adminloginservice;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public Adminlogincontroller(Adminloginservice adminloginservice, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.adminloginservice = adminloginservice;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String adminSignup(@RequestBody AdminloginDto adminloginDto) {
        AdminloginDto savedAdmin = adminloginservice.adminsignup(adminloginDto);
        return "Admin created with ID: " + savedAdmin.getId();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdminloginDto adminloginDto) {
        Optional<Adminlogin> adminloginOpt = adminloginservice.findByEmail(adminloginDto.getEmail());
        if (adminloginOpt.isPresent()) {
            Adminlogin adminlogin = adminloginOpt.get();

            if (passwordEncoder.matches(adminloginDto.getPassword(), adminlogin.getPassword())) {
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("role", adminlogin.getRole().name());

                String token = jwtService.generateToken(adminlogin.getEmail(), extraClaims);

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
