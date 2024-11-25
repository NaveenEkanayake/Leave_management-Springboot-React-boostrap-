package Leave_management.example.Leave.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeavemanagementDto {
    private Long id;
    private String customeremail;
    private String leaveType;
    private int days;
    private String description;
    private String action = "pending";
}

