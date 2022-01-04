package ir.maktab.finalproject.dto.output;

import ir.maktab.finalproject.entity.Request;
import ir.maktab.finalproject.entity.Specialist;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Builder
@Setter
@Getter
public class OfferOutputDTO {

    private Long id;

    private long specialistId;

    private Date registerDate;

    private Double price;

    private Double executionPeriod;

    private Date beginning;

    private Long requestId;
}
