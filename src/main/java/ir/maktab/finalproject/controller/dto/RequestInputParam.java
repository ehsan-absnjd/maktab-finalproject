package ir.maktab.finalproject.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@ToString
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
    @Future
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date executionDate;

    @NotNull
    private String address;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;
}
