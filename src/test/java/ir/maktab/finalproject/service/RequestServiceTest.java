package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class RequestServiceTest {

    Double customerCredit = 1000d;
    Double specialistCredit = 300d;
    Double orderPrice=500d;

    @Autowired
    RequestService service;

    @Autowired
    CustomerService customerService;

    @Autowired
    SpecialistService specialistService;

    @Autowired
    OfferService offerService;

    @Test
    public void whenAddingNewRequest_shouldBeAbleToRetrieveIt(){
        Customer customer = saveCustomerAndReturn();
        Request saved = saveRequestAndReturn(customer);
        Optional<Request> retrieved = service.findById(saved.getId());
        assertNotNull(retrieved.get());
        assertEquals(saved , retrieved.get());
    }

    @Test
    public void whenSettlingRequest_customerAndSpecialistsBalanceShouldBeAsExpected(){
        Specialist specialist = saveSpecialistAndReturn();
        Customer customer = saveCustomerAndReturn();
        Request request = saveRequestAndReturn(customer);
        Offer offer = Offer.builder().specialist(specialist).registerDate(new Date()).request(request).beginning(new Date())
                .executionPeriod(1d).price(orderPrice).build();
        Offer saved = offerService.save(offer);
        request.setSelectedOffer(offer);
        service.save(request);
        service.settle(request);
        assertEquals(RequestStatus.PAID ,service.findById(request.getId()).get().getStatus() );
        assertEquals(customerCredit-orderPrice, customerService.findById(customer.getId()).get().getCredit() );
        assertEquals(specialistCredit+orderPrice, specialistService.findById(specialist.getId()).get().getCredit() );
    }

    public Customer saveCustomerAndReturn(){
        Customer customer = Customer.builder().firstName("ali").lastName("reza")
                .email("ali@rezaei.ir").password("12345678a")
                .registrationDate(new Date()).status(UserStatus.NEW).credit(customerCredit).build();
        return customerService.save(customer);
    }

    public Specialist saveSpecialistAndReturn(){
        Specialist specialist = Specialist.builder().firstName("hoseyn").lastName("moharami")
                .email("hoseyn@moharami.ir").password("12345678a")
                .registrationDate(new Date()).status(UserStatus.NEW).credit(specialistCredit).build();
        return specialistService.save(specialist);
    }

    public Request saveRequestAndReturn(Customer customer){
        Request request = Request.builder().description("description").address("tehran").status(RequestStatus.WAITING_FOR_OFFERS)
                .customer(customer).executionDate(new Date()).offeredPrice(0d).registerDate(new Date()).build();
        return service.save(request);
    }

}