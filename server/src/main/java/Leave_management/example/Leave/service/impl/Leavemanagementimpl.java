package Leave_management.example.Leave.service.impl;

import Leave_management.example.Leave.dto.EmployeeDto;
import Leave_management.example.Leave.dto.LeavemanagementDto;
import Leave_management.example.Leave.entity.Employee;
import Leave_management.example.Leave.entity.Leavemanagement;
import Leave_management.example.Leave.exception.ResourceNotFoundException;
import Leave_management.example.Leave.mapper.LeavemanagementMapper;
import Leave_management.example.Leave.repository.Leavemanagementrepository;
import Leave_management.example.Leave.service.Leavemanagementservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Leavemanagementimpl implements Leavemanagementservice {

    private final Leavemanagementrepository leavemanagementRepository;

    @Autowired
    public Leavemanagementimpl(Leavemanagementrepository leavemanagementRepository) {
        this.leavemanagementRepository = leavemanagementRepository;
    }

    @Override
    public LeavemanagementDto applyLeave(LeavemanagementDto leavemanagementDto) {
        // Convert DTO to entity
        Leavemanagement leavemanagement = LeavemanagementMapper.mapToLeavemanagement(leavemanagementDto);
        leavemanagement.setAction("pending");
        Leavemanagement savedLeave = leavemanagementRepository.save(leavemanagement);
        return LeavemanagementMapper.mapToLeavemanagementDto(savedLeave);
    }

    @Override
    public LeavemanagementDto getLeaveStatus(Long id) {
        Leavemanagement leavemanagement = leavemanagementRepository.findById(id).orElse(null);

        if (leavemanagement != null) {
            return LeavemanagementMapper.mapToLeavemanagementDto(leavemanagement);
        }
        return null;
    }

    @Override
    public List<LeavemanagementDto> getAllLeaves() {
        List<Leavemanagement> employees= leavemanagementRepository.findAll();

        return employees.stream().map((employee -> LeavemanagementMapper.mapToLeavemanagementDto(employee)))
                .collect(Collectors.toList());

    }

    @Override
    public LeavemanagementDto updateLeave(Long id, LeavemanagementDto updatedLeave) {
        Leavemanagement leavemanagement=leavemanagementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave does not exist with the given ID"));
        leavemanagement.setLeave_type(updatedLeave.getLeaveType());
        leavemanagement.setDays(updatedLeave.getDays());
        leavemanagement.setDescription(updatedLeave.getDescription());
        Leavemanagement updatedLeaveObj = leavemanagementRepository.save(leavemanagement);
        return LeavemanagementMapper.mapToLeavemanagementDto(updatedLeaveObj);
    }


    @Override
    public void deleteLeave(Long Id) {


       Leavemanagement leavemanagement = leavemanagementRepository.findById(Id).orElseThrow(
                () -> new ResourceNotFoundException("Leave is not exist with given ID ")
        );

        leavemanagementRepository.deleteById(Id);

    }

    @Override
    public LeavemanagementDto updateLeaveStatus(Long id, String action) {
        Leavemanagement leavemanagement = leavemanagementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with the given ID"));

        leavemanagement.setAction(action);
        Leavemanagement updatedLeave = leavemanagementRepository.save(leavemanagement);

        return LeavemanagementMapper.mapToLeavemanagementDto(updatedLeave);
    }


}
