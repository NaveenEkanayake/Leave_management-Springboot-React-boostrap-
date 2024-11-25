package Leave_management.example.Leave.repository;

import Leave_management.example.Leave.entity.Leavemanagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Leavemanagementrepository  extends JpaRepository<Leavemanagement , Long> {
}
