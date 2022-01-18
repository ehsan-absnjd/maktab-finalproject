package ir.maktab.finalproject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInputDTO {
    @NotNull(message = "email should not be null")
    String email;

    @NotNull(message = "password should not be null")
    String password;
}
