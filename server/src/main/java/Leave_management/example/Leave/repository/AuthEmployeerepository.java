package Leave_management.example.Leave.repository;

import Leave_management.example.Leave.entity.AuthEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthEmployeerepository  extends JpaRepository<AuthEmployee,Long> {
    Optional<AuthEmployee> findByEmail(String email);
}
