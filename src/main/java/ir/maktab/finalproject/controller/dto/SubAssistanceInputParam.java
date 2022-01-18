package ir.maktab.finalproject.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Setter
@Getter
public class SubAssistanceInputParam {
    @NotNull
    private String title;

    @NotNull
    private Double basePrice;

    @NotNull
    private String description;
}
