package ir.maktab.finalproject.dto.output;

import ir.maktab.finalproject.entity.Request;
import ir.maktab.finalproject.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
public class CustomerOutputDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Date registrationDate;

    private UserStatus status;

    private Double credit;

}
