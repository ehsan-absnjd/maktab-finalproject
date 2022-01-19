package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.service.dto.input.*;
import ir.maktab.finalproject.service.dto.output.*;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.exception.RequestNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void whenGettingRelevantRequestForUser_shouldBeAbleToGetTheMatchingByAssistance(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        SpecialistInputDTO specialistInputDTO = helper.getSpecialistInputDTO1();
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        SpecialistOutputDTO savedSpecialist = specialistService.save(specialistInputDTO);
        SpecialistOutputDTO savedSpecialist2 = specialistService.save(specialistInputDTO2);
        specialistService.addAssistance(savedSpecialist.getId() , assistanceId);
        List<RequestOutputDTO> firstList = service.findForSpecialist(savedSpecialist.getId(), PageRequest.of(0, 10));
        List<RequestOutputDTO> secondList = service.findForSpecialist(savedSpecialist2.getId(), PageRequest.of(0, 10));
        assertEquals(1, firstList.size());
        assertEquals(0 , secondList.size());
    }

    @Test
    public void whenAddingRequestForCustomer_shouldBeAbleToGetIt() {
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        List<RequestOutputDTO> requestOutputDTOList = service.findByCustomerId(customerId, RequestStatus.WAITING_FOR_OFFERS ,  PageRequest.of(0, 10));
        List<RequestOutputDTO> list2 = service.findByCustomerId(1001L, RequestStatus.WAITING_FOR_OFFERS ,PageRequest.of(0, 10));
        assertEquals(1, requestOutputDTOList.size());
        assertEquals(0, list2.size());
    }

    @Test
    public void whenSelectingOrderForRequest_shouldBeAbleToGetRequestForSpecialist(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        SpecialistOutputDTO specialistOutputDTO = specialistService.save(helper.getSpecialistInputDTO1());
        specialistService.addAssistance(specialistOutputDTO.getId() , assistanceId);
        OfferOutputDTO offerOutputDTO = offerService.save(saved.getId(), helper.getOfferInputDTO1(specialistOutputDTO.getId(), 200000d));
        service.selectOffer(saved.getId() ,offerOutputDTO.getId());
        List<RequestOutputDTO> requestOutputDTOList = service.findBySpecialistId(specialistOutputDTO.getId() , RequestStatus.WAITING_ARRIVAL ,PageRequest.of(0,10));
        assertEquals(requestOutputDTOList.size() , 1);
    }

    @Test
    public void whenSelectingByUserId_retrievedRequestByCustomerAndSpecialistShouldBeSame() {
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        SpecialistOutputDTO specialistOutputDTO = specialistService.save(helper.getSpecialistInputDTO1());
        specialistService.addAssistance(specialistOutputDTO.getId(), assistanceId);
        OfferOutputDTO offerOutputDTO = offerService.save(saved.getId(), helper.getOfferInputDTO1(specialistOutputDTO.getId(), 200000d));
        service.selectOffer(saved.getId(), offerOutputDTO.getId());
        service.markBegun(saved.getId());
        service.markDone(saved.getId());
        List<RequestOutputDTO> customers = service.findByUserId(customerId, PageRequest.of(0, 10));
        List<RequestOutputDTO> specialists = service.findByUserId(specialistOutputDTO.getId(), PageRequest.of(0, 10));
        assertEquals(customers.get(0).getId() , specialists.get(0).getId());
    }

    @Test
    public void whenGettingByParameters_theResultsShouldBeOk(){
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO saved = service.save(requestInputDTO1);
        SpecialistOutputDTO specialistOutputDTO = specialistService.save(helper.getSpecialistInputDTO1());
        specialistService.addAssistance(specialistOutputDTO.getId(), assistanceId);
        OfferOutputDTO offerOutputDTO = offerService.save(saved.getId(), helper.getOfferInputDTO1(specialistOutputDTO.getId(), 200000d));
        service.selectOffer(saved.getId(), offerOutputDTO.getId());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("requeststatus" , "WAITING_ARRIVAL");
        List<RequestOutputDTO> byParameterMap = service.findByParameterMap(parameters);
        parameters.put("requeststatus" , "BEGUN");
        List<RequestOutputDTO> byParameterMap1 = service.findByParameterMap(parameters);
        assertEquals(1, byParameterMap.size());
        assertEquals(0, byParameterMap1.size());
    }


}