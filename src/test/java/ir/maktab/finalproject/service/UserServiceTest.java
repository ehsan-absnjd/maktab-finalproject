package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.service.dto.input.*;
import ir.maktab.finalproject.service.dto.output.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class UserServiceTest {
    @Autowired
    UserService service;

    @Autowired
    CustomerService customerService;

    @Autowired
    SpecialistService specialistService;

    @Autowired
    AssistanceService assistanceService;

    @Autowired
    SubAssistanceService subAssistanceService;

    @Autowired
    RequestService requestService;

    @Autowired
    OfferService offerService;

    Long specialistId;
    String specialistEmail;
    Long customerId;
    String customerEmail;

    @Autowired
    TestHelper helper;

    @BeforeEach
    public void setup(){
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        CustomerOutputDTO savedCustomer = customerService.save(customerInputDTO1);
        customerEmail=customerInputDTO1.getEmail();
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO savedSpecialist = specialistService.save(specialistInputDTO1);
        specialistEmail=specialistInputDTO1.getEmail();
        specialistId = savedSpecialist.getId();
        customerId = savedCustomer.getId();
    }

    @Test
    public void whenSavingNewUser_shouldBeAbleToRetrieveIt(){
        UserOutputDTO user = service.findById(specialistId);
        assertNotNull(user);
    }

    @Test
    public void whenRetrievingUsers_itShouldContainTheFiltered(){
        HashMap<String, String> parameters = new HashMap<>();
        AssistanceOutputDTO assistanceOutputDTO = assistanceService.save(helper.getAssistanceInputDTO1());
        specialistService.addAssistance(specialistId, assistanceOutputDTO.getId());
        parameters.put("assistance" ,String.valueOf(assistanceOutputDTO.getId()));
        List<UserOutputDTO> all = service.findByParameters(parameters);
        assertFalse(all.stream().filter(a-> a instanceof CustomerOutputDTO ).findAny().isPresent() );
        assertTrue(all.stream().filter(a-> a instanceof SpecialistOutputDTO ).findAny().isPresent() );
    }

    @Test
    public void whenGettingReport_shouldReturnCorrectSpecialist(){
        HashMap<String, String> parameters = new HashMap<>();
        AssistanceOutputDTO assistanceOutputDTO = assistanceService.save(helper.getAssistanceInputDTO1());
        specialistService.addAssistance(specialistId, assistanceOutputDTO.getId());
        SubAssistanceOutputDTO subAssistanceOutputDTO = subAssistanceService.save(assistanceOutputDTO.getId(), helper.getSubAssistanceInputDTO1());
        RequestOutputDTO requestOutputDTO = requestService.save(helper.getRequestInputDTO1(subAssistanceOutputDTO.getId(), customerId));
        OfferOutputDTO offerOutputDTO = offerService.save(requestOutputDTO.getId(), helper.getOfferInputDTO1(specialistId, 20000d));
        requestService.selectOffer(requestOutputDTO.getId() , offerOutputDTO.getId());
        parameters.put("mindonecount" ,String.valueOf(1));
        List<UserOutputDTO> reportByParameters = service.getReportByParameters(parameters);
        parameters.put("mindonecount" ,String.valueOf(2));
        List<UserOutputDTO> reportByParameters1 = service.getReportByParameters(parameters);
        assertEquals(1 , reportByParameters.size());
        assertEquals(0 , reportByParameters1.size());
    }

    @Test
    public void whenGettingReport_shouldReturnCorrectCustomer(){
        HashMap<String, String> parameters = new HashMap<>();
        AssistanceOutputDTO assistanceOutputDTO = assistanceService.save(helper.getAssistanceInputDTO1());
        specialistService.addAssistance(specialistId, assistanceOutputDTO.getId());
        SubAssistanceOutputDTO subAssistanceOutputDTO = subAssistanceService.save(assistanceOutputDTO.getId(), helper.getSubAssistanceInputDTO1());
        RequestOutputDTO requestOutputDTO = requestService.save(helper.getRequestInputDTO1(subAssistanceOutputDTO.getId(), customerId));
        OfferOutputDTO offerOutputDTO = offerService.save(requestOutputDTO.getId(), helper.getOfferInputDTO1(specialistId, 20000d));
        requestService.selectOffer(requestOutputDTO.getId() , offerOutputDTO.getId());
        parameters.put("minreceivedcount" ,String.valueOf(1));
        List<UserOutputDTO> reportByParameters = service.getReportByParameters(parameters);
        parameters.put("minreceivedcount" ,String.valueOf(2));
        List<UserOutputDTO> reportByParameters1 = service.getReportByParameters(parameters);
        assertEquals(1 , reportByParameters.size());
        assertEquals(0 , reportByParameters1.size());
    }

    @Test
    public void whenVerifyingSpecialist_itsStatusShouldBeWaitingApproval(){
        service.verify(specialistId, specialistEmail );
        SpecialistOutputDTO specialist = specialistService.findById(specialistId);
        assertEquals(UserStatus.WAITING_APPROVAL , specialist.getStatus());
    }

    @Test
    public void whenVerifyingCustomer_itsStatusShouldBeApproved(){
        service.verify(customerId, customerEmail );
        CustomerOutputDTO customer = customerService.findById(customerId);
        assertEquals(UserStatus.APPROVED , customer.getStatus());
    }
}