package Leave_management.example.Leave.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Leave_Managment")

public class Leavemanagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_email_id")
    private String customeremail;
    @Column(name = "leave_type")
    private String Leave_type;
    @Column(name = "days")
    private int days;
    @Column(name = "description")
    private String description;
    @Column(name = "action", nullable=false)
    private  String action="pending";
}
