package Leave_management.example.Leave.service;
import Leave_management.example.Leave.dto.AuthEmployeeDto;
import Leave_management.example.Leave.entity.AuthEmployee;
import java.util.Optional;

import java.util.Optional;

public interface AuthEmployeeservice {

   AuthEmployeeDto SignEmployee(AuthEmployeeDto authEmployeeDto);
   Optional<AuthEmployee> findByEmail(String email);
   void  DeleteAuthEmployee(Long Id);
}
