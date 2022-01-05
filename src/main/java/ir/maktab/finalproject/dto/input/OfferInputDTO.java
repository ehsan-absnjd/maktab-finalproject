package ir.maktab.finalproject.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Setter
@Getter
public class OfferInputDTO {
    @NotNull
    private Long specialistId;

    @NotNull
    private Double price;

    @NotNull
    private Double executionPeriod;

    @NotNull
    private Date beginning;
}
