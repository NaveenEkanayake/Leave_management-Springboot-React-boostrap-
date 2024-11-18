package Leave_management.example.Leave.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public class UpdateEmployeeEmail {
    @Autowired
    private JavaMailSender mailSender;

    public void sendUpdatedEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("leavemanagement2111@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            mailSender.send(message);
            System.out.println("Mail Sent Successfully");
        } catch (MailException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
