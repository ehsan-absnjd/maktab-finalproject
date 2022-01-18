package ir.maktab.finalproject.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class User{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @Size(min = 8)
    @Pattern( regexp = ".*[0-9].*")
    private String password;

    @NotNull
    private Date registrationDate;

    @NotNull
    private UserStatus status;

    @NotNull
    private Double credit;

    @NotNull
    String role;
}

