package Leave_management.example.Leave.repository;

import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByEmail(String email);
}
