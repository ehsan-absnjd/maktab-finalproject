package ir.maktab.finalproject.dto.output;

import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Offer;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.entity.SubAssistance;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RequestOutputDTO {

    private Long id;

    private CustomerOutputDTO customer;

    private SubAssistanceOutputDTO subAssistance;

    private Double offeredPrice;

    private String description;

    private Date registerDate;

    private Date executionDate;

    private String address;

    private RequestStatus status;

    private OfferOutputDTO selectedOffer;

    private Double points;

    private String comment;
}
