package ir.maktab.finalproject.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User{
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
    @Pattern( regexp = "\\d")
    private String password;

    @NotNull
    private Date registrationDate;

    @NotNull
    private UserStatus status;

    @NotNull
    private Double credit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(registrationDate, user.registrationDate) && status == user.status && Objects.equals(credit, user.credit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, registrationDate, status, credit);
    }
}

