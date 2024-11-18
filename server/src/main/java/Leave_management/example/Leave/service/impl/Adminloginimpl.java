package Leave_management.example.Leave.service.impl;

import Leave_management.example.Leave.dto.AdminloginDto;
import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.exception.ResourceNotFoundException;
import Leave_management.example.Leave.mapper.Adminloginmapper;
import Leave_management.example.Leave.repository.Adminloginrepository;
import Leave_management.example.Leave.service.Adminloginservice;
import Leave_management.example.Leave.util.Jwtutil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class Adminloginimpl implements Adminloginservice {

    private final Adminloginrepository adminloginrepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwtutil jwtutil;

    @Override
    public AdminloginDto adminsignup(AdminloginDto adminloginDto) {
        Adminlogin adminlogin = Adminloginmapper.mapToAdminlogin(adminloginDto);
        String encodedPassword = passwordEncoder.encode(adminlogin.getPassword());
        adminlogin.setPassword(encodedPassword);
        Adminlogin newAdmin = adminloginrepository.save(adminlogin);
        return Adminloginmapper.mapToAdminloginDto(newAdmin);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Optional<Adminlogin> findByEmail(String email) {
        return adminloginrepository.findByEmail(email);
    }
}
