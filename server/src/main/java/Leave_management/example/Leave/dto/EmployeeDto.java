package Leave_management.example.Leave.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Dto is used to share the data between client and Server this is like a response to the API
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}