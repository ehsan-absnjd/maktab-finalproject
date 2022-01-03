package ir.maktab.finalproject.dto.input;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class OfferInputDTO {

    @NotNull
    private Double price;

    @NotNull
    private Double executionPeriod;

    @NotNull
    private Date beginning;

}
