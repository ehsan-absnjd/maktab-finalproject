package ir.maktab.finalproject.dto.output;

import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Offer;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.entity.SubAssistance;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
}
