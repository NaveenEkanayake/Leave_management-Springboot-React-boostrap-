package Leave_management.example.Leave.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthEmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
