package Leave_management.example.Leave.dto;

import Leave_management.example.Leave.Roles.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminloginDto {
    private Long id;
    private String fullname;
    private String email;
    private Roles role;
    private String password;
}