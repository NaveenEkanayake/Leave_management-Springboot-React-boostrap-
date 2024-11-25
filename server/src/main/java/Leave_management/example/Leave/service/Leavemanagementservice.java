package Leave_management.example.Leave.service;

import Leave_management.example.Leave.dto.EmployeeDto;
import Leave_management.example.Leave.dto.LeavemanagementDto;

import java.util.List;

public interface Leavemanagementservice {
    LeavemanagementDto applyLeave(LeavemanagementDto leavemanagementDto);
    LeavemanagementDto getLeaveStatus(Long id);
    List<LeavemanagementDto> getAllLeaves();
    LeavemanagementDto updateLeave(Long Id,LeavemanagementDto updatedLeave);
    void deleteLeave(Long Id);
    LeavemanagementDto updateLeaveStatus(Long id, String action);

}
