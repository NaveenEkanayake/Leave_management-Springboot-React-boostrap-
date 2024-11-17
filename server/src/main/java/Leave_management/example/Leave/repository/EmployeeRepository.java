package Leave_management.example.Leave.repository;

import Leave_management.example.Leave.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByEmail(String email);
}
