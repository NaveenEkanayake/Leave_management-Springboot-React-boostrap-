package Leave_management.example.Leave.controller;


import Leave_management.example.Leave.Email.LeavemanagementEmail;
import Leave_management.example.Leave.dto.LeavemanagementDto;
import Leave_management.example.Leave.exception.ResourceNotFoundException;
import Leave_management.example.Leave.service.Leavemanagementservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class Leavemanagementcontroller {

    @Autowired
    private  Leavemanagementservice leavemanagementservice;
    @Autowired
    private LeavemanagementEmail  leavemanagementEmail;

    private static final Logger logger = LoggerFactory.getLogger(Leavemanagementcontroller .class);

    @PostMapping("/apply")
    public ResponseEntity<LeavemanagementDto> applyLeave(@RequestBody LeavemanagementDto leavemanagementDto) {
        LeavemanagementDto appliedLeave = leavemanagementservice.applyLeave(leavemanagementDto);
        String subject = "Leave Request Confirmation";
        String body = "Dear " + appliedLeave.getCustomeremail() + ",\n\n" +
                "We are pleased to inform you that your leave request has been successfully submitted.\n\n" +
                "Leave Details:\n" +
                "---------------------------------\n" +
                "Leave Type: " + appliedLeave.getLeaveType() + "\n" +
                "Leave Duration: " + appliedLeave.getDays() + " days\n" +
                "Leave Status: " + appliedLeave.getAction() + "\n" +
                "---------------------------------\n\n" +
                "You will be notified once the status of your leave request is updated.\n\n" +
                "Thank you for submitting your leave request. If you have any further questions or need assistance, please do not hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "The Leave Management Team\n";

        //  confirmation email
        leavemanagementEmail.sendLeavemanagementEmail(appliedLeave.getCustomeremail(), subject, body);
        return new ResponseEntity<>(appliedLeave, HttpStatus.CREATED);
    }
    @GetMapping("/status/{id}")
    public ResponseEntity<LeavemanagementDto> getLeaveStatus(@PathVariable Long id) {
        LeavemanagementDto leaveStatus = leavemanagementservice.getLeaveStatus(id);
        return leaveStatus != null ? new ResponseEntity<>(leaveStatus, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get All Employees
    @GetMapping("/getAllLeaves")
    public ResponseEntity<Map<String, Object>> getAllLeaves() {
        List<LeavemanagementDto> Leave = leavemanagementservice.getAllLeaves();
        Map<String, Object> response = new HashMap<>();
        response.put("data", Leave );
        response.put("message", "Your Leaves  Retrieved Successfully!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/UpdateLeave/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody LeavemanagementDto updatedLeave) {

        try {
            LeavemanagementDto leavemanagementDto = leavemanagementservice.updateLeave(id, updatedLeave);
            Map<String, Object> response = new HashMap<>();
            response.put("data", leavemanagementDto);
            response.put("message", " Your Leave updated successfully!");

            String subject = "Your Information has been Updated in the Leave Management System";
            String body = String.format(
                    "Hello %s,\n\n" +
                            "Your Leave information has been successfully updated in our Leave Management System. Here are the updated details:\n\n" +
                            "Leave Type: %s %s\n" +
                            "Days you Applied: %s\n" +
                            "Description: %s\n\n" +
                            "If you have any questions or require further assistance, please do not hesitate to reach out.\n\n" +
                            "Best regards,\n" +
                            "Leave Management Team",
                    leavemanagementDto.getCustomeremail(),
                    leavemanagementDto.getLeaveType(),
                    leavemanagementDto.getDays(),
                    leavemanagementDto.getDescription()
            );


            leavemanagementEmail.sendLeavemanagementEmail(leavemanagementDto.getCustomeremail(), subject, body);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred while updating Leave: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Error occurred while updating Leave. Please try again later."));
        }
    }

    //delete Employee
    @DeleteMapping("deleteLeave/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long Id){
        leavemanagementservice.deleteLeave(Id);
        return  ResponseEntity.ok("Your Leave deleted Successfully");
    }

    @PatchMapping("/updateStatus/{id}")
    public ResponseEntity<Map<String, Object>> updateLeaveStatus(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> actionUpdate) {
        try {
            String action = actionUpdate.get("action");
            if (action == null || (!action.equalsIgnoreCase("approved") && !action.equalsIgnoreCase("rejected"))) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid action value. Use 'approved' or 'rejected'."));
            }

            LeavemanagementDto updatedLeave = leavemanagementservice.updateLeaveStatus(id, action);
            Map<String, Object> response = new HashMap<>();
            response.put("data", updatedLeave);
            response.put("message", "Leave status updated successfully!");

            String subject = "Leave Request Status Update";
            String body = String.format(
                    "Hello %s,\n\n" +
                            "Your leave request status has been updated to: %s.\n\n" +
                            "Thank you for your patience.\n\n" +
                            "Best regards,\n" +
                            "Leave Management Team",
                    updatedLeave.getCustomeremail(),
                    action
            );

            leavemanagementEmail.sendLeavemanagementEmail(updatedLeave.getCustomeremail(), subject, body);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.error("Leave not found: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Leave not found with the given ID."));
        } catch (Exception e) {
            logger.error("Error occurred while updating leave status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error occurred while updating leave status. Please try again later."));
        }
    }


}

