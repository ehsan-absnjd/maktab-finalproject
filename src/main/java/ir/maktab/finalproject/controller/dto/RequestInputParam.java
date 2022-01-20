package ir.maktab.finalproject.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class RequestInputParam {
    @NotNull
    private Long subAssistanceId;

    @NotNull
    private Long customerId;

    @NotNull
    private Double offeredPrice;

    @NotNull
    private String description;

    @NotNull
    private Date executionDate;

    @NotNull
    private String address;
}
