package ir.maktab.finalproject.dto.output;

import ir.maktab.finalproject.entity.Request;
import ir.maktab.finalproject.entity.Specialist;

import java.util.Date;

public class OfferOutputDTO {

    private Long id;

    private Specialist specialist;

    private Date registerDate;

    private Double price;

    private Double executionPeriod;

    private Date beginning;

    private Request request;
}
