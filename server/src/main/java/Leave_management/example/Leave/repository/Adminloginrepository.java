package Leave_management.example.Leave.repository;

import Leave_management.example.Leave.entity.Adminlogin;
import Leave_management.example.Leave.entity.AuthEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Adminloginrepository extends JpaRepository<Adminlogin,Long> {
    Optional<Adminlogin> findByEmail(String email);
}
