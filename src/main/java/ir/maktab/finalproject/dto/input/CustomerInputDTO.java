package ir.maktab.finalproject.dto.input;

import ir.maktab.finalproject.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Setter
@Getter
public class CustomerInputDTO {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    private String email;

    @Size(min = 8)
    @Pattern( regexp = ".*[0-9].*")
    private String password;

    @NotNull
    private UserStatus status;

    @NotNull
    private Double credit;

}
