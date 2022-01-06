package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.dto.input.RequestInputDTO;
import ir.maktab.finalproject.dto.input.SubAssistanceInputDTO;
import ir.maktab.finalproject.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.dto.output.SubAssistanceOutputDTO;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.exception.RequestNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class RequestServiceTest {
    @Autowired
    RequestService service;

    @Autowired
    CustomerService customerService;

    @Autowired
    AssistanceService assistanceService;

    @Autowired
    SubAssistanceService subAssistanceService;

    @Autowired
    SpecialistService specialistService;

    @Autowired
    OfferService offerService;

    @Autowired
    TestHelper helper;

    Long customerId;
    Long assistanceId;
    Long subAssistanceId;

    @BeforeEach
    public void setup(){
        AssistanceInputDTO assistanceInputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceOutputDTO savedAssistance = assistanceService.save(assistanceInputDTO1);
        assistanceId =savedAssistance.getId();
        SubAssistanceInputDTO subAssistanceInputDTO1 = helper.getSubAssistanceInputDTO1();
        SubAssistanceOutputDTO savedSubAssistance = subAssistanceService.save(assistanceId, subAssistanceInputDTO1);
        subAssistanceId=savedSubAssistance.getId();
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        CustomerOutputDTO savedCustomer = customerService.save(customerInputDTO1);
        customerId = savedCustomer.getId();
    }

    @Test
    public void whenAddingNewRequest_shouldBeAbleToRetrieveIt(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        RequestOutputDTO retrieved = service.findById(saved.getId());
        helper.testEquality(requestInputDTO1 , retrieved);
        assertEquals(RequestStatus.WAITING_FOR_OFFERS , retrieved.getStatus());
    }

    @Test
    public void whenAddingTwoRequest_shouldBeAbleToRetrieveThem(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestInputDTO requestInputDTO2 = helper.getRequestInputDTO2(subAssistanceId, customerId);
        service.save(requestInputDTO1);
        service.save(requestInputDTO2);
        List<RequestOutputDTO> all1 = service.findAll();
        List<RequestOutputDTO> all2 = service.findAll(PageRequest.of(0, 10));
        assertEquals(2, all1.size());
        assertEquals(2,all2.size());
    }

    @Test
    public void whenUpdatingRequest_requestDataShouldBeUpdated(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestInputDTO requestInputDTO2 = helper.getRequestInputDTO2(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        service.update(saved.getId() , requestInputDTO2);
        RequestOutputDTO retrieved = service.findById(saved.getId());
        helper.testEquality(requestInputDTO2 , retrieved);
    }

    @Test
    public void whenGettingDeletedRequest_shouldThrowException(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        service.removeById(saved.getId());
        assertThrows(RequestNotFoundException.class , ()-> service.findById(saved.getId()));
    }
}