package ir.maktab.finalproject.service.dto.input;

import lombok.*;
import javax.validation.constraints.NotNull;

@Builder
@Setter
@Getter
public class AssistanceInputDTO {
    @NotNull
    private String title;
}
