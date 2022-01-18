package ir.maktab.finalproject.service.dto.output;

import ir.maktab.finalproject.entity.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Date registrationDate;

    private UserStatus status;

    private Double credit;

    private String role;
}
