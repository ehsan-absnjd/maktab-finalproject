package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.entity.*;
import ir.maktab.finalproject.exception.InvalidSpecialistOfferException;
import ir.maktab.finalproject.exception.OfferNotFoundException;
import ir.maktab.finalproject.service.dto.input.*;
import ir.maktab.finalproject.service.dto.output.*;
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
class OfferServiceTest {
    @Autowired
    OfferService service;

    @Autowired
    RequestService requestService;

    @Autowired
    CustomerService customerService;

    @Autowired
    SpecialistService specialistService;

    @Autowired
    AssistanceService assistanceService;

    @Autowired
    SubAssistanceService subAssistanceService;

    @Autowired
    TestHelper helper;

    Long customerId;
    Long assistanceId;
    Long subAssistanceId;
    Long specialistId;
    Long requestId1;
    Long requestId2;

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
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO savedSpecialist = specialistService.save(specialistInputDTO1);
        specialistId = savedSpecialist.getId();
        specialistService.addAssistance(specialistId,assistanceId);
        RequestInputDTO requestInputDTO1 = helper.getRequestInputDTO1(subAssistanceId, customerId);
        RequestOutputDTO savedRequest = requestService.save(requestInputDTO1);
        requestId1 =savedRequest.getId();
        RequestInputDTO requestInputDTO2 = helper.getRequestInputDTO2(subAssistanceId, customerId);
        RequestOutputDTO savedRequest2 = requestService.save(requestInputDTO2);
        requestId2=savedRequest2.getId();
    }

    @Test
    public void whenSavingOfferForSpecialistWithNotRelatedAssistance_shouldThrowException(){
        specialistService.removeAssistance(specialistId ,assistanceId);
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 200000d);
        assertThrows(InvalidSpecialistOfferException.class , ()-> service.save(requestId1, offerInputDTO1));
    }

    @Test
    public void whenSavingNewOffer_shouldBeAbleToRetrieveIt(){
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 200000d);
        OfferOutputDTO saved = service.save(requestId1, offerInputDTO1);
        OfferOutputDTO retrieved = service.findByRequestIdAndOfferId(requestId1, saved.getId());
        helper.testEquality(offerInputDTO1 , retrieved);
    }

    @Test
    public void whenSavingTwoOffers_shouldBeAbleToRetrieveThem(){
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 200000d);
        OfferInputDTO offerInputDTO2 = helper.getOfferInputDTO2(specialistId, 100000d);
        service.save(requestId1, offerInputDTO1);
        service.save(requestId1, offerInputDTO2);
        List<OfferOutputDTO> all1 = service.findByRequestId(requestId1);
        List<OfferOutputDTO> all2 = service.findByRequestId(requestId1, PageRequest.of(0, 10));
        List<OfferOutputDTO> all3 = service.findByRequestIdOrderByPointsDesc(requestId1, PageRequest.of(0, 10));
        List<OfferOutputDTO> all4 = service.findByRequestIdOrderByPriceAsc(requestId1, PageRequest.of(0, 10));
        assertTrue(all4.get(0).getPrice() < all4.get(1).getPrice());
        assertEquals(2,all1.size());
        assertEquals(2, all2.size());
        assertEquals(2, all3.size());
        assertEquals(2, all4.size());
    }

    @Test
    public void whenUpdatingOffer_itsDataShouldBeUpdated(){
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 200000d);
        OfferInputDTO offerInputDTO2 = helper.getOfferInputDTO2(specialistId, 300000d);
        OfferOutputDTO saved = service.save(requestId1, offerInputDTO1);
        service.update(requestId1, saved.getId() , offerInputDTO2);
        OfferOutputDTO retrieved = service.findByRequestIdAndOfferId(requestId1, saved.getId());
        helper.testEquality(offerInputDTO2 , retrieved);
    }

    @Test
    public void whenGettingDeletedOffer_shouldThrowException(){
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 200000d);
        OfferOutputDTO saved = service.save(requestId1, offerInputDTO1);
        service.removeById(requestId1,saved.getId());
        assertThrows(OfferNotFoundException.class , ()->service.findByRequestIdAndOfferId(requestId1, saved.getId()));
    }

    @Test
    public void newlyCreatedRequest_shouldHaveWaitingForOffersStatus(){
        RequestOutputDTO request = requestService.findById(requestId1);
        assertEquals(RequestStatus.WAITING_FOR_OFFERS , request.getStatus());
    }

    @Test
    public void whenAddingOfferForRequest_itsStatusShouldBeWaitingForSelect(){
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 5000d);
        service.save(requestId1, offerInputDTO1);
        RequestOutputDTO request = requestService.findById(requestId1);
        assertEquals(RequestStatus.WAITING_FOR_SELECT , request.getStatus());
    }

    @Test
    public void whenSelectingOffer_itsStatusShouldBeWaitingForArrival(){
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 5000d);
        OfferOutputDTO saved = service.save(requestId1, offerInputDTO1);
        requestService.selectOffer(requestId1, saved.getId());
        RequestOutputDTO request = requestService.findById(requestId1);
        assertEquals(RequestStatus.WAITING_ARRIVAL , request.getStatus());
    }

    @Test
    public void whenSettlingRequest_customerAndSpecialistsBalanceShouldBeAsExpected(){
        customerService.addCredit(customerId, 100000d);
        Double specialistCredit = specialistService.findById(specialistId).getCredit();
        Double customerCredit = customerService.findById(customerId).getCredit();
        Double offerPrice = 5000d;
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, offerPrice);
        OfferOutputDTO saved = service.save(requestId1, offerInputDTO1);
        requestService.selectOffer(requestId1, saved.getId());
        requestService.markBegun(requestId1);
        requestService.markDone(requestId1);
        requestService.pay(requestId1);
        assertEquals(specialistCredit+offerPrice*.7 , specialistService.findById(specialistId).getCredit() , .001);
        assertEquals(customerCredit-offerPrice , customerService.findById(customerId).getCredit() , .001);
    }

    @Test
    public void whenSettlingTwoRequestForOneSpecialist_customerPointsShouldBeTheAverage(){
        double points1= 7d;
        double points2=4d;
        OfferInputDTO offerInputDTO1 = helper.getOfferInputDTO1(specialistId, 50000d);
        OfferOutputDTO saved1 = service.save(requestId1, offerInputDTO1);
        OfferInputDTO offerInputDTO2 = helper.getOfferInputDTO2(specialistId, 50000d);
        OfferOutputDTO saved2 = service.save(requestId2, offerInputDTO2);
        requestService.selectOffer(requestId1, saved1.getId());
        requestService.markBegun(requestId1);
        requestService.markDone(requestId1);
        requestService.evaluate(requestId1, EvaluationInputDTO.builder().points(points1).comment("it was good").build());
        requestService.selectOffer(requestId2, saved2.getId());
        requestService.markBegun(requestId2);
        requestService.markDone(requestId2);
        requestService.evaluate(requestId2, EvaluationInputDTO.builder().points(points2).comment("it was not bad").build());
        SpecialistOutputDTO specialist = specialistService.findById(specialistId);
        assertEquals((points1+points2)/2 , specialist.getPoints() , 0.001);
    }
}