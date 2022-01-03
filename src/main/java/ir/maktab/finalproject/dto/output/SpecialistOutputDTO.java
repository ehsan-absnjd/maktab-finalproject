package ir.maktab.finalproject.dto.output;

import ir.maktab.finalproject.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Setter
@Getter
public class SpecialistOutputDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Date registrationDate;

    private UserStatus status;

    private Double credit;

    private String photoURL;

    private Double points;

}
