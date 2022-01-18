package ir.maktab.finalproject;

import ir.maktab.finalproject.service.dto.input.*;
import ir.maktab.finalproject.service.dto.output.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestHelper {
    public CustomerInputDTO getCustomerInputDTO1(){
        return CustomerInputDTO.builder()
                .firstName("ali")
                .lastName("rezaei")
                .email("ali@rezaei.ir")
                .password("12345678a")
                .credit(100000d).build();
    }

    public CustomerInputDTO getCustomerInputDTO2(){
        return CustomerInputDTO.builder()
                .firstName("mohsen")
                .lastName("bashiri")
                .email("mohsen.bashiri@gmail.com")
                .password("12345678b")
                .credit(200000d).build();
    }

    public SpecialistInputDTO getSpecialistInputDTO1() {
        return SpecialistInputDTO.builder()
                .firstName("mohammmad")
                .lastName("behzadeh")
                .email("mohammad12@gmail.com")
                .password("mohammad1")
                .credit(300d)
                .photoURL("fakeurl1")
                .build();
    }

    public AssistanceInputDTO getAssistanceInputDTO1(){
        return AssistanceInputDTO.builder()
                .title("title1")
                .build();
    }

    public AssistanceInputDTO getAssistanceInputDTO2(){
        return AssistanceInputDTO.builder()
                .title("title2")
                .build();
    }

    public SpecialistInputDTO getSpecialistInputDTO2() {
        return SpecialistInputDTO.builder()
                .firstName("gholam")
                .lastName("alavi")
                .email("g.alavi@gmail.com")
                .password("ghalghal323")
                .credit(100d)
                .photoURL("fakeurl2")
                .build();
    }

    public void testEquality(CustomerInputDTO inputDTO, CustomerOutputDTO outputDTO) {
        assertEquals(inputDTO.getFirstName() , outputDTO.getFirstName());
        assertEquals(inputDTO.getLastName() , outputDTO.getLastName());
        assertEquals(inputDTO.getEmail() , outputDTO.getEmail());
        assertEquals(inputDTO.getCredit() , outputDTO.getCredit());
    }

    public void testEquality(SpecialistInputDTO inputDTO, SpecialistOutputDTO outputDTO) {
        assertEquals(inputDTO.getFirstName(),outputDTO.getFirstName());
        assertEquals(inputDTO.getLastName(),outputDTO.getLastName());
        assertEquals(inputDTO.getEmail() , outputDTO.getEmail());
        assertEquals(inputDTO.getCredit() , outputDTO.getCredit());
        assertEquals(inputDTO.getPhotoURL() , outputDTO.getPhotoURL());
    }

    public void testEquality(SpecialistOutputDTO target, SpecialistOutputDTO other) {
        assertEquals(target.getId(), other.getId());
        assertEquals(target.getFirstName() , other.getFirstName());
        assertEquals(target.getLastName() , other.getLastName());
        assertEquals(target.getEmail() , other.getEmail());
        assertEquals(target.getStatus() , other.getStatus());
        assertEquals(target.getPhotoURL() , other.getPhotoURL());
        assertEquals(target.getCredit() , other.getCredit());
        assertEquals(target.getPoints() , other.getPoints());
    }

    public SubAssistanceInputDTO getSubAssistanceInputDTO1() {
        return SubAssistanceInputDTO.builder()
                .basePrice(10d)
                .description("some description 1")
                .title("some title 1")
                .build();
    }

    public SubAssistanceInputDTO getSubAssistanceInputDTO2() {
        return SubAssistanceInputDTO.builder()
                .basePrice(12d)
                .description("some description 2")
                .title("some title 2")
                .build();
    }

    public void testEquality(SubAssistanceInputDTO target, SubAssistanceOutputDTO other) {
        assertEquals(target.getTitle() , other.getTitle());
        assertEquals(target.getDescription() , other.getDescription());
        assertEquals(target.getBasePrice() , other.getBasePrice());
    }

    public RequestInputDTO getRequestInputDTO1(Long subAssistanceId , Long customerId) {
        return RequestInputDTO.builder()
                .subAssistanceId(subAssistanceId)
                .customerId(customerId)
                .address("some address")
                .executionDate(new Date(2022,11,11,15,30))
                .address("tehran valiasr")
                .description("some job you can't refuse")
                .offeredPrice(200000d)
                .build();
    }

    public RequestInputDTO getRequestInputDTO2(Long subAssistanceId , Long customerId) {
        return RequestInputDTO.builder()
                .subAssistanceId(subAssistanceId)
                .customerId(customerId)
                .address("another address")
                .executionDate(new Date(2022,10,12,12,30))
                .address("tehran shariati")
                .description("another job you can't refuse")
                .offeredPrice(300000d)
                .build();
    }

    public void testEquality(RequestInputDTO target, RequestOutputDTO other) {
        assertEquals(target.getCustomerId() , other.getCustomerId());
        assertEquals(target.getSubAssistanceId() , other.getSubAssistanceId());
        assertEquals(target.getAddress() , other.getAddress());
        assertEquals(target.getExecutionDate() , other.getExecutionDate());
        assertEquals(target.getDescription() , other.getDescription());
        assertEquals(target.getOfferedPrice() , other.getOfferedPrice());
    }

    public OfferInputDTO getOfferInputDTO1(Long specialistId , Double price) {
        return OfferInputDTO.builder()
                .beginning(new Date(2023,11,11,13,30))
                .executionPeriod(1d)
                .specialistId(specialistId)
                .price(price)
                .build();
    }

    public OfferInputDTO getOfferInputDTO2(Long specialistId , Double price) {
        return OfferInputDTO.builder()
                .beginning(new Date(2022,12,11,13,30))
                .executionPeriod(1.5d)
                .specialistId(specialistId)
                .price(price)
                .build();
    }

    public void testEquality(OfferInputDTO target, OfferOutputDTO other) {
        assertEquals(target.getBeginning() , other.getBeginning());
        assertEquals(target.getPrice() , other.getPrice());
        assertEquals(target.getSpecialistId() , other.getSpecialistId());
    }
}
