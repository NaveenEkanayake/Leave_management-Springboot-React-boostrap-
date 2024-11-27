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
@Entity //specify the Jpa entity
@Table(name = "employees") // making the Table
public class Employee {
    @Id //specifying the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //configuring the primary key
    private Long id;

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "email",unique = true, nullable = false)
    private String email;
    @Column(name = "role")
    private  String role;

}