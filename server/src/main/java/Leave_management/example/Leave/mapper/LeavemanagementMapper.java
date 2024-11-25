package Leave_management.example.Leave.mapper;

import Leave_management.example.Leave.dto.LeavemanagementDto;
import Leave_management.example.Leave.entity.Leavemanagement;

public class LeavemanagementMapper {

    public static LeavemanagementDto mapToLeavemanagementDto(Leavemanagement leavemanagement) {
        return new LeavemanagementDto(
                leavemanagement.getId(),
                leavemanagement.getCustomeremail(),
                leavemanagement.getLeave_type(),
                leavemanagement.getDays(),
                leavemanagement.getDescription(),
                leavemanagement.getAction()
        );
    }

    public static Leavemanagement mapToLeavemanagement(LeavemanagementDto leavemanagementDto) {
        Leavemanagement leavemanagement = new Leavemanagement();
        leavemanagement.setId(leavemanagementDto.getId());
        leavemanagement.setCustomeremail(leavemanagementDto.getCustomeremail());
        leavemanagement.setLeave_type(leavemanagementDto.getLeaveType());
        leavemanagement.setDays(leavemanagementDto.getDays());
        leavemanagement.setDescription(leavemanagementDto.getDescription());
        leavemanagement.setAction(leavemanagementDto.getAction());

        return leavemanagement;
    }
}
