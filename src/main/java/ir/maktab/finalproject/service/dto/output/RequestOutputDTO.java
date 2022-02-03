package ir.maktab.finalproject.service.dto.output;

import ir.maktab.finalproject.entity.RequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Setter
@Getter
public class RequestOutputDTO {
    private Long id;

    private Long customerId;

    private Long subAssistanceId;

    private Double offeredPrice;

    private String description;

    private Date registerDate;

    private Date executionDate;

    private String address;

    private RequestStatus status;

    private Long selectedOffer;

    private Double points;

    private String comment;

    private Double longitude;

    private Double latitude;
}
