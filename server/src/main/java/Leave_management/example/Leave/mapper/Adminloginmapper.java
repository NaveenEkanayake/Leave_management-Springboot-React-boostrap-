package Leave_management.example.Leave.mapper;

import Leave_management.example.Leave.dto.AdminloginDto;
import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.entity.AuthEmployee;

public class Adminloginmapper {
    public static AdminloginDto mapToAdminloginDto(Adminlogin adminlogin) {
        return new AdminloginDto(
                adminlogin.getId(),
                adminlogin.getFullname(),
                adminlogin.getEmail(),
                adminlogin.getRole(),
                null
        );
    }

    public static Adminlogin mapToAdminlogin(AdminloginDto adminloginDto) {
        Adminlogin adminlogin = new Adminlogin();
        adminlogin.setFullname(adminloginDto.getFullname());
        adminlogin.setEmail(adminloginDto.getEmail());
        adminlogin.setPassword(adminloginDto.getPassword());
        adminlogin.setRole(adminloginDto.getRole());
        return adminlogin;
    }
}
