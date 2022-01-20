package ir.maktab.finalproject.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Setter
@Getter
@NoArgsConstructor
public class AddAssistanceInputParam {
    @NotNull
    private Long assistanceId;
}
