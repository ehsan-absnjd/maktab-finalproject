package ir.maktab.finalproject.dto.input;

import ir.maktab.finalproject.entity.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class RequestInputDTO {

    @NotNull
    private Double offeredPrice;

    @NotNull
    private String description;

    @NotNull
    private Date executionDate;

    @NotNull
    private String address;

    @NotNull
    private RequestStatus status;

}
