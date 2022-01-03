package ir.maktab.finalproject.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Setter
@Getter
public class SubAssistanceInputDTO {

    @NotNull
    private String title;

    @NotNull
    private Double basePrice;

    @NotNull
    private String description;

}
