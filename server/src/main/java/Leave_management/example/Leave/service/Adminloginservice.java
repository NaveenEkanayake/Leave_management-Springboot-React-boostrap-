package Leave_management.example.Leave.service;

import Leave_management.example.Leave.dto.AdminloginDto;
import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.entity.AuthEmployee;

import java.util.Optional;

public interface Adminloginservice {
    AdminloginDto adminsignup(AdminloginDto adminloginDto);
    Optional<Adminlogin> findByEmail(String email);
}
